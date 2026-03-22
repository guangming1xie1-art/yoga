package com.yoga.backend.module.session.controller;

import com.yoga.backend.module.session.service.SessionManagementService;
import com.yoga.common.dto.session.SessionVO;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "管理端-课程管理", description = "课程管理相关接口")
@RestController
@RequestMapping("/api/backend/session")
@RequiredArgsConstructor
public class SessionManagementController {
    
    private final SessionManagementService sessionManagementService;

    @Operation(summary = "更新课程状态")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateSessionStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String reason) {
        boolean success = sessionManagementService.updateSessionStatus(id, status, reason);
        return Result.ok(success);
    }

    @Operation(summary = "获取课程详情")
    @GetMapping("/{id}")
    public Result<SessionVO> getSessionDetail(@PathVariable Long id) {
        SessionVO session = sessionManagementService.getDetail(id);
        return Result.ok(session);
    }

    @Operation(summary = "分页查询课程")
    @GetMapping("/list")
    public Result<PageResponse<SessionVO>> listSessions(@Valid PageRequest pageRequest) {
        PageResponse<SessionVO> sessions = sessionManagementService.listSessions(pageRequest);
        return Result.ok(sessions);
    }
}