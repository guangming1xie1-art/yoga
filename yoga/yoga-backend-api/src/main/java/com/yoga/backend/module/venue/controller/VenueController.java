package com.yoga.backend.module.venue.controller;

import com.yoga.common.dto.venue.VenueDTO;
import com.yoga.common.entity.Venue;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.Result;
import com.yoga.backend.module.venue.service.VenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "场馆管理", description = "场馆CRUD")
@RestController
@RequestMapping("/api/backend/venue")
@RequiredArgsConstructor
public class VenueController {

    private final VenueService venueService;

    @Operation(summary = "场馆列表")
    @GetMapping("/list")
    public Result<PageResponse<Venue>> list(@Valid PageRequest pageRequest) {
        return Result.ok(venueService.listVenues(pageRequest));
    }

    @Operation(summary = "场馆详情")
    @GetMapping("/{id}")
    public Result<Venue> detail(@PathVariable Long id) {
        return Result.ok(venueService.getDetail(id));
    }

    @Operation(summary = "创建场馆")
    @PostMapping
    public Result<Venue> create(@Valid @RequestBody VenueDTO dto) {
        return Result.ok(venueService.create(dto));
    }

    @Operation(summary = "更新场馆")
    @PutMapping("/{id}")
    public Result<Venue> update(@PathVariable Long id, @Valid @RequestBody VenueDTO dto) {
        return Result.ok(venueService.update(id, dto));
    }

    @Operation(summary = "删除场馆")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        venueService.delete(id);
        return Result.ok();
    }
}
