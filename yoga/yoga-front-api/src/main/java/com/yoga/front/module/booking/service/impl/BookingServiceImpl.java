package com.yoga.front.module.booking.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.dto.booking.BookingCreateRequest;
import com.yoga.common.dto.booking.BookingExportDTO;
import com.yoga.common.dto.booking.BookingVO;
import com.yoga.common.entity.Booking;
import com.yoga.common.entity.CourseSession;
import com.yoga.common.entity.CourseTemplate;
import com.yoga.common.entity.Coach;
import com.yoga.common.entity.Order;
import com.yoga.common.entity.User;
import com.yoga.common.entity.Venue;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.ResultCode;
import com.yoga.front.module.booking.mapper.BookingMapper;
import com.yoga.front.module.booking.service.BookingService;
import com.yoga.front.module.order.mapper.OrderMapper;
import com.yoga.front.module.session.mapper.SessionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    
    private final BookingMapper bookingMapper;
    private final SessionMapper sessionMapper;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingVO create(Long userId, BookingCreateRequest request) {
        try {
            log.info("开始创建预约，用户ID: {}, 课程ID: {}", userId, request.getSessionId());

            CourseSession session = sessionMapper.selectById(request.getSessionId());
            if (session == null) {
                throw new BusinessException(ResultCode.SESSION_NOT_FOUND);
            }

            if (!"SCHEDULED".equals(session.getStatus())) {
                throw new BusinessException(ResultCode.SESSION_CANCELLED);
            }

            if (session.getBookedCount() >= session.getCapacity()) {
                throw new BusinessException(ResultCode.SESSION_FULL);
            }

            if (session.getStartTime().isBefore(LocalDateTime.now())) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "不能预约已开始的课程");
            }

            if (session.getStartTime().isAfter(LocalDateTime.now().plusDays(7))) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "只能预约7天内的课程");
            }

            LambdaQueryWrapper<Booking> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Booking::getUserId, userId)
                   .eq(Booking::getSessionId, request.getSessionId())
                   .in(Booking::getStatus, List.of("PENDING", "CONFIRMED"));
            Booking existingBooking = bookingMapper.selectOne(wrapper);
            if (existingBooking != null) {
                throw new BusinessException(ResultCode.BOOKING_ALREADY_EXISTS);
            }

            Booking booking = new Booking();
            booking.setUserId(userId);
            booking.setSessionId(request.getSessionId());
            booking.setStatus("PENDING");
            bookingMapper.insert(booking);

            session.setBookedCount(session.getBookedCount() + 1);
            sessionMapper.updateById(session);

            String qrCode = UUID.randomUUID().toString();
            booking.setQrCode(qrCode);
            booking.setQrExpireAt(session.getStartTime().plusHours(2));
            bookingMapper.updateById(booking);

            Order order = createOrder(userId, booking.getId(), session.getPrice());
            booking.setOrderId(order.getId());
            bookingMapper.updateById(booking);

            log.info("预约创建成功，预约ID: {}, 订单ID: {}", booking.getId(), order.getId());
            return convertToVO(booking, session, order);

        } catch (BusinessException e) {
            log.warn("预约创建失败，业务异常: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("预约创建失败，系统异常", e);
            throw new BusinessException(ResultCode.INTERNAL_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long userId, Long bookingId) {
        try {
            log.info("开始取消预约，用户ID: {}, 预约ID: {}", userId, bookingId);

            Booking booking = bookingMapper.selectById(bookingId);
            if (booking == null) {
                throw new BusinessException(ResultCode.BOOKING_NOT_FOUND);
            }

            if (!booking.getUserId().equals(userId)) {
                throw new BusinessException(ResultCode.FORBIDDEN);
            }

            if (!"PENDING".equals(booking.getStatus()) && !"CONFIRMED".equals(booking.getStatus())) {
                throw new BusinessException(ResultCode.BOOKING_CANNOT_CANCEL);
            }

            CourseSession session = sessionMapper.selectById(booking.getSessionId());
            if (session.getStartTime().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BusinessException(ResultCode.BOOKING_CANNOT_CANCEL);
            }

            booking.setStatus("CANCELLED");
            booking.setCancelBy("USER");
            booking.setCancelReason("用户主动取消");
            bookingMapper.updateById(booking);

            session.setBookedCount(Math.max(0, session.getBookedCount() - 1));
            sessionMapper.updateById(session);

            if (booking.getOrderId() != null) {
                refundOrder(booking.getOrderId());
            }

            log.info("预约取消成功，预约ID: {}", bookingId);

        } catch (BusinessException e) {
            log.warn("预约取消失败，业务异常: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("预约取消失败，系统异常", e);
            throw new BusinessException(ResultCode.INTERNAL_ERROR);
        }
    }

    @Override
    public PageResponse<BookingVO> myBookings(Long userId, PageRequest pageRequest) {
        try {
            Page<Booking> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());

            LambdaQueryWrapper<Booking> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Booking::getUserId, userId)
                   .orderByDesc(Booking::getCreatedAt);

            Page<Booking> result = bookingMapper.selectPage(page, wrapper);

            List<BookingVO> voList = result.getRecords().stream()
                    .map(booking -> {
                        CourseSession session = sessionMapper.selectById(booking.getSessionId());
                        Order order = booking.getOrderId() != null ? orderMapper.selectById(booking.getOrderId()) : null;
                        return convertToVO(booking, session, order);
                    })
                    .collect(Collectors.toList());

            return PageResponse.of(voList, result.getTotal(), pageRequest.getPage(), pageRequest.getSize());

        } catch (Exception e) {
            log.error("查询预约列表失败，用户ID: {}", userId, e);
            throw new BusinessException(ResultCode.INTERNAL_ERROR);
        }
    }

    @Override
    public BookingVO getDetail(Long id) {
        try {
            Booking booking = bookingMapper.selectById(id);
            if (booking == null) {
                throw new BusinessException(ResultCode.BOOKING_NOT_FOUND);
            }

            CourseSession session = sessionMapper.selectById(booking.getSessionId());
            Order order = booking.getOrderId() != null ? orderMapper.selectById(booking.getOrderId()) : null;

            return convertToVO(booking, session, order);

        } catch (BusinessException e) {
            log.warn("查询预约详情失败，业务异常: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("查询预约详情失败，预约ID: {}", id, e);
            throw new BusinessException(ResultCode.INTERNAL_ERROR);
        }
    }

    @Override
    public void exportBookings(Long userId, HttpServletResponse response) throws IOException {
        try {
            // 获取用户的所有预约记录并转换为导出格式
            List<BookingExportDTO> bookings = getAllBookingsForExport(userId);
            
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("预约记录_" + System.currentTimeMillis(), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 导出Excel
            EasyExcel.write(response.getOutputStream(), BookingExportDTO.class)
                    .sheet("预约记录")
                    .doWrite(bookings);
        } catch (Exception e) {
            log.error("导出预约记录失败", e);
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            throw new IOException("导出预约记录失败");
        }
    }

    @Override
    public List<BookingVO> getAllBookings(Long userId) {
        try {
            LambdaQueryWrapper<Booking> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Booking::getUserId, userId);
            List<Booking> bookings = bookingMapper.selectList(wrapper);

            return bookings.stream()
                    .map(booking -> {
                        CourseSession session = sessionMapper.selectById(booking.getSessionId());
                        Order order = booking.getOrderId() != null ? orderMapper.selectById(booking.getOrderId()) : null;
                        return convertToVO(booking, session, order);
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("查询所有预约失败，用户ID: {}", userId, e);
            throw new BusinessException(ResultCode.INTERNAL_ERROR);
        }
    }
    
    private Order createOrder(Long userId, Long bookingId, BigDecimal amount) {
        Order order = new Order();
        order.setUserId(userId);
        order.setSessionId(bookingId);
        order.setOrderNo("ORD" + System.currentTimeMillis());
        order.setAmount(amount);
        order.setStatus("PENDING");
        orderMapper.insert(order);
        return order;
    }

    private void refundOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order != null && "PAID".equals(order.getStatus())) {
            order.setStatus("REFUNDING");
            order.setRefundAmount(order.getAmount());
            orderMapper.updateById(order);
        }
    }

    private BookingVO convertToVO(Booking booking, CourseSession session, Order order) {
        BookingVO vo = new BookingVO();
        vo.setId(booking.getId());
        vo.setSessionId(booking.getSessionId());
        vo.setPrice(order != null ? order.getAmount() : (session != null ? session.getPrice() : null));
        vo.setStatus(booking.getStatus());
        vo.setQrCode(booking.getQrCode());
        vo.setQrExpireAt(booking.getQrExpireAt());
        vo.setCreatedAt(booking.getCreatedAt());

        if (session != null) {
            vo.setSessionStartTime(session.getStartTime());
            vo.setSessionEndTime(session.getEndTime());
        }

        return vo;
    }
    
    /**
     * 获取用户的所有预约记录（用于导出Excel）
     * @param userId 用户ID
     * @return 预约记录列表（导出格式）
     */
    private List<BookingExportDTO> getAllBookingsForExport(Long userId) {
        // 查询用户的预约记录
        LambdaQueryWrapper<Booking> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Booking::getUserId, userId);
        List<Booking> bookings = bookingMapper.selectList(queryWrapper);
        
        // 转换为导出格式 - 简化处理，直接使用Booking实体的数据
        return bookings.stream().map(this::convertToExportDTO).collect(Collectors.toList());
    }
    
    /**
     * 将Booking实体转换为导出DTO
     * @param booking 预约实体
     * @return 导出DTO
     */
    private BookingExportDTO convertToExportDTO(Booking booking) {
        BookingExportDTO exportDTO = new BookingExportDTO();
        exportDTO.setId(booking.getId());
        exportDTO.setStatus(booking.getStatus());
        exportDTO.setCreatedAt(booking.getCreatedAt());
        // 这里只设置了Booking实体中直接有的字段，更完整的实现需要关联查询其他表
        return exportDTO;
    }
}