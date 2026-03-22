package com.yoga.backend.module.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.entity.Order;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;

public interface OrderService {

    PageResponse<Order> listOrders(PageRequest pageRequest);

    Order getDetail(Long id);

    void refund(Long id);
}
