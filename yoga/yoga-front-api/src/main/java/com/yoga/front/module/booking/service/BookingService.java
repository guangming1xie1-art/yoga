package com.yoga.front.module.booking.service;

import com.yoga.common.dto.booking.BookingCreateRequest;
import com.yoga.common.dto.booking.BookingVO;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface BookingService {
    BookingVO create(Long userId, BookingCreateRequest request);
    void cancel(Long userId, Long bookingId);
    PageResponse<BookingVO> myBookings(Long userId, PageRequest pageRequest);
    BookingVO getDetail(Long id);
    
    /**
     * 导出用户预约记录
     * @param userId 用户ID
     * @param response HTTP响应对象
     * @throws IOException
     */
    void exportBookings(Long userId, HttpServletResponse response) throws IOException;
    
    /**
     * 获取用户的所有预约记录（用于导出）
     * @param userId 用户ID
     * @return 预约记录列表
     */
    List<BookingVO> getAllBookings(Long userId);
}
