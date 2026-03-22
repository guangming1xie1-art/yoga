package com.yoga.front.module.user.controller;

import com.yoga.common.entity.User;
import com.yoga.common.result.Result;
import com.yoga.front.module.user.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理", description = "用户信息查询和更新")
@RestController
@RequestMapping("/api/front/user")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;

    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    @Cacheable(value = "user", key = "#userId", unless = "#result == null")
    public Result<User> getInfo(@AuthenticationPrincipal String userId) {
        User user = userMapper.selectById(Long.parseLong(userId));
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.ok(user);
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/info")
    @CacheEvict(value = "user", key = "#userId")
    public Result<Void> updateInfo(@AuthenticationPrincipal String userId, @RequestBody User user) {
        user.setId(Long.parseLong(userId));
        userMapper.updateById(user);
        return Result.ok();
    }
}
