package com.yoga.front.module.booking.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yoga.common.dto.booking.BookingCreateRequest;
import com.yoga.common.dto.booking.BookingExportDTO;
import com.yoga.common.dto.booking.BookingVO;
import com.yoga.common.entity.Booking;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.front.module.booking.mapper.BookingMapper;
import com.yoga.front.module.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    
    private final BookingMapper bookingMapper;

    @Override
    public BookingVO create(Long userId, BookingCreateRequest request) {
        throw new UnsupportedOperationException("TODO: create booking");
    }

    @Override
    public void cancel(Long userId, Long bookingId) {
        throw new UnsupportedOperationException("TODO: cancel booking");
    }

    @Override
    public PageResponse<BookingVO> myBookings(Long userId, PageRequest pageRequest) {
        throw new UnsupportedOperationException("TODO: myBookings");
    }

    @Override
    public BookingVO getDetail(Long id) {
        throw new UnsupportedOperationException("TODO: getDetail");
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
        throw new UnsupportedOperationException("TODO: getAllBookings - 需要实现BookingVO转换逻辑");
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