package com.yoga.front.module.session.controller;

import com.yoga.common.dto.session.SessionVO;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.Result;
import com.yoga.front.module.session.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "课程列表（分页）", description = "查询可预约的课程列表，支持按场馆、类别、日期筛选")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功")
    })
    @GetMapping("/list")
    public Result<PageResponse<SessionVO>> list(
            @Parameter(description = "场馆ID") @RequestParam(required = false) Long venueId,
            @Parameter(description = "课程类别") @RequestParam(required = false) String category,
            @Parameter(description = "日期（yyyy-MM-dd）") @RequestParam(required = false) String date,
            @Valid PageRequest pageRequest) {
        return Result.ok(sessionService.listSessions(venueId, category, date, pageRequest));
    }

    @Operation(summary = "课程详情", description = "查询课程的详细信息，包括课程模板、教练、场馆等信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "404", description = "课程不存在")
    })
    @GetMapping("/{id}")
    public Result<SessionVO> detail(
            @Parameter(description = "排期ID") @PathVariable Long id) {
        return Result.ok(sessionService.getDetail(id));
    }

    @Operation(summary = "搜索课程", description = "根据关键词搜索课程")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "搜索成功")
    })
    @GetMapping("/search")
    public Result<PageResponse<SessionVO>> search(
            @Parameter(description = "搜索关键词") @RequestParam String keyword,
            @Valid PageRequest pageRequest) {
        return Result.ok(sessionService.search(keyword, pageRequest));
    }

    @Operation(summary = "课程评价列表", description = "查询课程的评价列表")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功")
    })
    @GetMapping("/{id}/reviews")
    public Result<PageResponse<?>> reviews(
            @Parameter(description = "排期ID") @PathVariable Long id,
            @Valid PageRequest pageRequest) {
        return Result.ok(sessionService.listReviews(id, pageRequest));
    }
}
