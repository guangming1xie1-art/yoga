package com.yoga.backend.module.coach.controller;

import com.yoga.common.entity.Coach;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.Result;
import com.yoga.backend.module.coach.service.CoachService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "教练管理", description = "教练CRUD操作")
@RestController
@RequestMapping("/api/backend/coach")
@RequiredArgsConstructor
public class CoachController {

    private final CoachService coachService;

    @Operation(summary = "教练列表")
    @GetMapping("/list")
    public Result<PageResponse<Coach>> list(@Valid PageRequest pageRequest) {
        return Result.ok(coachService.listCoaches(pageRequest));
    }

    @Operation(summary = "教练详情")
    @GetMapping("/{id}")
    public Result<Coach> detail(@PathVariable Long id) {
        return Result.ok(coachService.getDetail(id));
    }

    @Operation(summary = "新增教练")
    @PostMapping
    public Result<Coach> create(@Valid @RequestBody Coach coach) {
        return Result.ok(coachService.create(coach));
    }

    @Operation(summary = "更新教练")
    @PutMapping("/{id}")
    public Result<Coach> update(@PathVariable Long id, @Valid @RequestBody Coach coach) {
        return Result.ok(coachService.update(id, coach));
    }

    @Operation(summary = "删除教练")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        coachService.delete(id);
        return Result.ok();
    }
}
