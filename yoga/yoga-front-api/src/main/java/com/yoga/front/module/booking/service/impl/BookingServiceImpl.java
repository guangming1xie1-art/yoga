package com.yoga.front.module.booking.service.impl;

import com.yoga.common.dto.booking.BookingCreateRequest;
import com.yoga.common.dto.booking.BookingVO;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.front.module.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

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
}
