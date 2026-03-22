package com.yoga.backend.module.user.controller;

import com.yoga.common.entity.User;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.Result;
import com.yoga.backend.module.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户管理", description = "用户查询和状态管理")
@RestController
@RequestMapping("/api/backend/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "用户列表")
    @GetMapping("/list")
    public Result<PageResponse<User>> list(@Valid PageRequest pageRequest) {
        return Result.ok(userService.listUsers(pageRequest));
    }

    @Operation(summary = "用户详情")
    @GetMapping("/{id}")
    public Result<User> detail(@PathVariable Long id) {
        return Result.ok(userService.getDetail(id));
    }

    @Operation(summary = "更新用户状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer disabled) {
        userService.updateStatus(id, disabled);
        return Result.ok();
    }
}
