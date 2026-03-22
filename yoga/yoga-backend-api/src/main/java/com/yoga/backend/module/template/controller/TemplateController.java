package com.yoga.backend.module.template.controller;

import com.yoga.common.entity.CourseTemplate;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.Result;
import com.yoga.backend.module.template.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "课程模板管理", description = "课程模板CRUD操作")
@RestController
@RequestMapping("/api/backend/template")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @Operation(summary = "课程模板列表")
    @GetMapping("/list")
    public Result<PageResponse<CourseTemplate>> list(@Valid PageRequest pageRequest) {
        return Result.ok(templateService.listTemplates(pageRequest));
    }

    @Operation(summary = "课程模板详情")
    @GetMapping("/{id}")
    public Result<CourseTemplate> detail(@PathVariable Long id) {
        return Result.ok(templateService.getDetail(id));
    }

    @Operation(summary = "新增课程模板")
    @PostMapping
    public Result<CourseTemplate> create(@Valid @RequestBody CourseTemplate template) {
        return Result.ok(templateService.create(template));
    }

    @Operation(summary = "更新课程模板")
    @PutMapping("/{id}")
    public Result<CourseTemplate> update(@PathVariable Long id, @Valid @RequestBody CourseTemplate template) {
        return Result.ok(templateService.update(id, template));
    }

    @Operation(summary = "删除课程模板")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        templateService.delete(id);
        return Result.ok();
    }
}
