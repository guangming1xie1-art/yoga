package com.yoga.front.module.session.controller;

import com.yoga.common.dto.session.SessionVO;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.Result;
import com.yoga.front.module.session.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "课程排期", description = "课程列表/详情/搜索/评价")
@RestController
@RequestMapping("/api/front/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @Operation(summary = "课程列表（分页）")
    @GetMapping("/list")
    public Result<PageResponse<SessionVO>> list(
            @RequestParam(required = false) Long venueId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String date,
            @Valid PageRequest pageRequest) {
        return Result.ok(sessionService.listSessions(venueId, category, date, pageRequest));
    }

    @Operation(summary = "课程详情")
    @GetMapping("/{id}")
    public Result<SessionVO> detail(
            @Parameter(description = "排期ID") @PathVariable Long id) {
        return Result.ok(sessionService.getDetail(id));
    }

    @Operation(summary = "搜索课程")
    @GetMapping("/search")
    public Result<PageResponse<SessionVO>> search(
            @RequestParam String keyword,
            @Valid PageRequest pageRequest) {
        return Result.ok(sessionService.search(keyword, pageRequest));
    }

    @Operation(summary = "课程评价列表")
    @GetMapping("/{id}/reviews")
    public Result<PageResponse<?>> reviews(
            @PathVariable Long id,
            @Valid PageRequest pageRequest) {
        return Result.ok(sessionService.listReviews(id, pageRequest));
    }
}
