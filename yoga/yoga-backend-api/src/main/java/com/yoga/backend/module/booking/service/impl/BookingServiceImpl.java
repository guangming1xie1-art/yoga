package com.yoga.backend.module.booking.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.entity.Booking;
import com.yoga.common.entity.CourseSession;
import com.yoga.common.entity.Order;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.ResultCode;
import com.yoga.backend.module.booking.mapper.BookingMapper;
import com.yoga.backend.module.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingMapper bookingMapper;
    private final SessionMapper sessionMapper;
    private final OrderMapper orderMapper;

    @Override
    public PageResponse<Booking> listBookings(PageRequest pageRequest) {
        Page<Booking> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        LambdaQueryWrapper<Booking> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Booking::getCreatedAt);
        Page<Booking> result = bookingMapper.selectPage(page, wrapper);
        return PageResponse.of(result);
    }

    @Override
    public Booking getDetail(Long id) {
        Booking booking = bookingMapper.selectById(id);
        if (booking == null) {
            throw new BusinessException(ResultCode.BOOKING_NOT_FOUND);
        }
        return booking;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long id) {
        Booking booking = getDetail(id);

        if (!"PENDING".equals(booking.getStatus()) && !"CONFIRMED".equals(booking.getStatus())) {
            throw new BusinessException(ResultCode.BOOKING_CANNOT_CANCEL);
        }

        CourseSession session = sessionMapper.selectById(booking.getSessionId());
        if (session != null && session.getStartTime().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BusinessException(ResultCode.BOOKING_CANNOT_CANCEL, "课程开始前2小时内不可取消");
        }

        booking.setStatus("CANCELLED");
        booking.setCancelBy("ADMIN");
        booking.setCancelReason("管理员取消");
        bookingMapper.updateById(booking);

        if (session != null) {
            session.setBookedCount(Math.max(0, session.getBookedCount() - 1));
            sessionMapper.updateById(session);
        }

        if (booking.getOrderId() != null) {
            Order order = orderMapper.selectById(booking.getOrderId());
            if (order != null && "PAID".equals(order.getStatus())) {
                order.setStatus("REFUNDING");
                orderMapper.updateById(order);
            }
        }

        log.info("管理员取消预约成功，预约ID: {}", id);
    }
}
