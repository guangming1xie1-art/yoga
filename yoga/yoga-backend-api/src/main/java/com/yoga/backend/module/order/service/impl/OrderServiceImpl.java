package com.yoga.backend.module.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.entity.Booking;
import com.yoga.common.entity.Order;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.ResultCode;
import com.yoga.backend.module.booking.mapper.BookingMapper;
import com.yoga.backend.module.order.mapper.OrderMapper;
import com.yoga.backend.module.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final BookingMapper bookingMapper;

    @Override
    public PageResponse<Order> listOrders(PageRequest pageRequest) {
        Page<Order> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Order::getCreatedAt);
        Page<Order> result = orderMapper.selectPage(page, wrapper);
        return PageResponse.of(result);
    }

    @Override
    public Order getDetail(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(ResultCode.ORDER_NOT_FOUND);
        }
        return order;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refund(Long id) {
        Order order = getDetail(id);

        if (!"PAID".equals(order.getStatus())) {
            throw new BusinessException(ResultCode.ORDER_REFUND_FAILED, "订单状态不允许退款");
        }

        order.setStatus("REFUNDED");
        order.setRefundAmount(order.getAmount());
        order.setRefundedAt(LocalDateTime.now());
        orderMapper.updateById(order);

        Booking booking = bookingMapper.selectById(order.getSessionId());
        if (booking != null && "CONFIRMED".equals(booking.getStatus())) {
            booking.setStatus("CANCELLED");
            booking.setCancelBy("ADMIN");
            booking.setCancelReason("订单退款");
            bookingMapper.updateById(booking);
        }

        log.info("管理员退款成功，订单ID: {}", id);
    }
}
