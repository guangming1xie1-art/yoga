package com.yoga.backend.module.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.entity.User;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;

public interface UserService {

    PageResponse<User> listUsers(PageRequest pageRequest);

    User getDetail(Long id);

    void updateStatus(Long id, Integer disabled);
}
