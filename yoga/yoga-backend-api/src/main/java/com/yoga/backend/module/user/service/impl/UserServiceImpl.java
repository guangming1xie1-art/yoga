package com.yoga.backend.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.entity.User;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.ResultCode;
import com.yoga.backend.module.user.mapper.UserMapper;
import com.yoga.backend.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public PageResponse<User> listUsers(PageRequest pageRequest) {
        Page<User> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getCreatedAt);
        Page<User> result = userMapper.selectPage(page, wrapper);
        return PageResponse.of(result);
    }

    @Override
    public User getDetail(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }
        return user;
    }

    @Override
    public void updateStatus(Long id, Integer disabled) {
        User user = getDetail(id);
        user.setDisabled(disabled);
        userMapper.updateById(user);
        log.info("更新用户状态成功，用户ID: {}, 状态: {}", id, disabled == 1 ? "禁用" : "启用");
    }
}
