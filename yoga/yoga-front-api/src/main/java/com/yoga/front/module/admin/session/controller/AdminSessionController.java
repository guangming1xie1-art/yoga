package com.yoga.front.module.admin.session.controller;

import com.yoga.common.dto.session.SessionVO;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.Result;
import com.yoga.front.module.admin.session.service.AdminSessionService;
import com.yoga.front.module.log.annotation.OperationLog;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "用户端-管理-课程管理", description = "管理端课程管理相关接口")
@RestController
@RequestMapping("/api/front/admin/session")
@RequiredArgsConstructor
public class AdminSessionController {
    
    private final AdminSessionService adminSessionService;

    @Operation(summary = "更新课程状态（管理端）")
    @PutMapping("/{id}/status")
    @OperationLog(module = "课程管理", action = "更新课程状态", description = "管理端更新课程状态（如取消、改期等）")
    public Result<Boolean> updateSessionStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String reason) {
        boolean success = adminSessionService.updateSessionStatus(id, status, reason);
        return Result.ok(success);
    }

    @Operation(summary = "获取课程详情（管理端）")
    @GetMapping("/{id}")
    @OperationLog(module = "课程管理", action = "查询课程详情", description = "管理端查询课程详细信息")
    public Result<SessionVO> getSessionDetail(@PathVariable Long id) {
        SessionVO session = adminSessionService.getDetail(id);
        return Result.ok(session);
    }

    @Operation(summary = "分页查询课程（管理端）")
    @GetMapping("/list")
    @OperationLog(module = "课程管理", action = "分页查询课程", description = "管理端分页查询课程列表")
    public Result<PageResponse<SessionVO>> listSessions(@Valid PageRequest pageRequest) {
        PageResponse<SessionVO> sessions = adminSessionService.listSessions(pageRequest);
        return Result.ok(sessions);
    }
}