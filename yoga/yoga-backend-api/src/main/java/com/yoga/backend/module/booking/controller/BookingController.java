package com.yoga.backend.module.booking.controller;

import com.yoga.common.entity.Booking;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.Result;
import com.yoga.backend.module.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "预约管理", description = "预约查询和取消")
@RestController
@RequestMapping("/api/backend/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "预约列表")
    @GetMapping("/list")
    public Result<PageResponse<Booking>> list(@Valid PageRequest pageRequest) {
        return Result.ok(bookingService.listBookings(pageRequest));
    }

    @Operation(summary = "预约详情")
    @GetMapping("/{id}")
    public Result<Booking> detail(@PathVariable Long id) {
        return Result.ok(bookingService.getDetail(id));
    }

    @Operation(summary = "取消预约")
    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Long id) {
        bookingService.cancel(id);
        return Result.ok();
    }
}
