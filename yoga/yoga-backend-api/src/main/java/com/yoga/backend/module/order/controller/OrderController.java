package com.yoga.backend.module.order.controller;

import com.yoga.common.entity.Order;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.Result;
import com.yoga.backend.module.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "订单管理", description = "订单查询和退款")
@RestController
@RequestMapping("/api/backend/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "订单列表")
    @GetMapping("/list")
    public Result<PageResponse<Order>> list(@Valid PageRequest pageRequest) {
        return Result.ok(orderService.listOrders(pageRequest));
    }

    @Operation(summary = "订单详情")
    @GetMapping("/{id}")
    public Result<Order> detail(@PathVariable Long id) {
        return Result.ok(orderService.getDetail(id));
    }

    @Operation(summary = "订单退款")
    @PostMapping("/{id}/refund")
    public Result<Void> refund(@PathVariable Long id) {
        orderService.refund(id);
        return Result.ok();
    }
}
