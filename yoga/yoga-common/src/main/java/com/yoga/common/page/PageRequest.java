package com.yoga.common.page;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 统一分页请求
 */
@Data
@Schema(description = "分页请求参数")
public class PageRequest {

    @Schema(description = "页码，从1开始", defaultValue = "1", example = "1")
    @Min(value = 1, message = "页码最小为1")
    private int page = 1;

    @Schema(description = "每页条数", defaultValue = "10", example = "10")
    @Min(value = 1, message = "每页条数最小为1")
    @Max(value = 100, message = "每页条数最大为100")
    private int size = 10;

    @Schema(description = "排序字段", example = "createdAt")
    private String sortBy;

    @Schema(description = "排序方向：asc/desc", defaultValue = "desc", example = "desc")
    private String sortDir = "desc";

    public long getOffset() {
        return (long) (page - 1) * size;
    }
}
