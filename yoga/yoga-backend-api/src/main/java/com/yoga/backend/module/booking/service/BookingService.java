package com.yoga.backend.module.booking.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.entity.Booking;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;

public interface BookingService {

    PageResponse<Booking> listBookings(PageRequest pageRequest);

    Booking getDetail(Long id);

    void cancel(Long id);
}
