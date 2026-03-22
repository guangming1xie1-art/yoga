package com.yoga.front.module.booking.controller;

import com.yoga.common.dto.booking.BookingCreateRequest;
import com.yoga.common.dto.booking.BookingVO;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.Result;
import com.yoga.front.module.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "预约管理", description = "预约/取消/列表")
@RestController
@RequestMapping("/api/front/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "创建预约", description = "用户创建课程预约，系统会自动创建订单")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "预约成功", 
                content = @Content(schema = @Schema(implementation = BookingVO.class))),
        @ApiResponse(responseCode = "400", description = "参数错误"),
        @ApiResponse(responseCode = "409", description = "课程已满或已预约")
    })
    @PostMapping
    public Result<BookingVO> create(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody BookingCreateRequest request) {
        return Result.ok(bookingService.create(Long.parseLong(userId), request));
    }

    @Operation(summary = "取消预约", description = "取消预约，课程开始前2小时内不可取消")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "取消成功"),
        @ApiResponse(responseCode = "400", description = "取消失败，课程即将开始")
    })
    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(
            @AuthenticationPrincipal String userId,
            @Parameter(description = "预约ID") @PathVariable Long id) {
        bookingService.cancel(Long.parseLong(userId), id);
        return Result.ok();
    }

    @Operation(summary = "我的预约列表", description = "查询当前登录用户的所有预约记录")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功")
    })
    @GetMapping("/my")
    public Result<PageResponse<BookingVO>> myBookings(
            @AuthenticationPrincipal String userId,
            @Valid PageRequest pageRequest) {
        return Result.ok(bookingService.myBookings(Long.parseLong(userId), pageRequest));
    }

    @Operation(summary = "预约详情", description = "查询预约的详细信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "404", description = "预约不存在")
    })
    @GetMapping("/{id}")
    public Result<BookingVO> detail(
            @Parameter(description = "预约ID") @PathVariable Long id) {
        return Result.ok(bookingService.getDetail(id));
    }
    
    @Operation(summary = "导出预约记录", description = "导出用户的预约记录为Excel文件")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "导出成功")
    })
    @GetMapping("/export")
    public void exportBookings(
            @AuthenticationPrincipal String userId, 
            HttpServletResponse response) throws IOException {
        bookingService.exportBookings(Long.parseLong(userId), response);
    }
}
