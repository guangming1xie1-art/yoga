package com.yoga.backend.module.schedule.controller;

import com.yoga.common.entity.CourseSchedule;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.Result;
import com.yoga.backend.module.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "排课管理", description = "排课规则CRUD和自动生成排期")
@RestController
@RequestMapping("/api/backend/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Operation(summary = "排课规则列表")
    @GetMapping("/list")
    public Result<PageResponse<CourseSchedule>> list(@Valid PageRequest pageRequest) {
        return Result.ok(scheduleService.listSchedules(pageRequest));
    }

    @Operation(summary = "排课规则详情")
    @GetMapping("/{id}")
    public Result<CourseSchedule> detail(@PathVariable Long id) {
        return Result.ok(scheduleService.getDetail(id));
    }

    @Operation(summary = "新增排课规则")
    @PostMapping
    public Result<CourseSchedule> create(@Valid @RequestBody CourseSchedule schedule) {
        return Result.ok(scheduleService.create(schedule));
    }

    @Operation(summary = "更新排课规则")
    @PutMapping("/{id}")
    public Result<CourseSchedule> update(@PathVariable Long id, @Valid @RequestBody CourseSchedule schedule) {
        return Result.ok(scheduleService.update(id, schedule));
    }

    @Operation(summary = "删除排课规则")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return Result.ok();
    }

    @Operation(summary = "生成课程排期")
    @PostMapping("/{id}/generate")
    public Result<Void> generateSessions(@PathVariable Long id) {
        scheduleService.generateSessions(id);
        return Result.ok();
    }
}
