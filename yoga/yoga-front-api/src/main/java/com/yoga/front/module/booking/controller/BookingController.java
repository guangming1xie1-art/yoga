package com.yoga.front.module.booking.controller;

import com.yoga.common.dto.booking.BookingCreateRequest;
import com.yoga.common.dto.booking.BookingVO;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.Result;
import com.yoga.front.module.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "预约管理", description = "预约/取消/列表")
@RestController
@RequestMapping("/api/front/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "创建预约")
    @PostMapping
    public Result<BookingVO> create(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody BookingCreateRequest request) {
        return Result.ok(bookingService.create(Long.parseLong(userId), request));
    }

    @Operation(summary = "取消预约")
    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(
            @AuthenticationPrincipal String userId,
            @PathVariable Long id) {
        bookingService.cancel(Long.parseLong(userId), id);
        return Result.ok();
    }

    @Operation(summary = "我的预约列表")
    @GetMapping("/my")
    public Result<PageResponse<BookingVO>> myBookings(
            @AuthenticationPrincipal String userId,
            @Valid PageRequest pageRequest) {
        return Result.ok(bookingService.myBookings(Long.parseLong(userId), pageRequest));
    }

    @Operation(summary = "预约详情")
    @GetMapping("/{id}")
    public Result<BookingVO> detail(@PathVariable Long id) {
        return Result.ok(bookingService.getDetail(id));
    }
}
