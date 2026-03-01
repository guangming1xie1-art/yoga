package com.yoga.front.module.booking.service;

import com.yoga.common.dto.booking.BookingCreateRequest;
import com.yoga.common.dto.booking.BookingVO;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;

public interface BookingService {
    BookingVO create(Long userId, BookingCreateRequest request);
    void cancel(Long userId, Long bookingId);
    PageResponse<BookingVO> myBookings(Long userId, PageRequest pageRequest);
    BookingVO getDetail(Long id);
}
