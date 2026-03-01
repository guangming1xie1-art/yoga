package com.yoga.common.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 统一分页响应
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页响应体")
public class PageResponse<T> {

    @Schema(description = "数据列表")
    private List<T> list;

    @Schema(description = "总条数", example = "100")
    private long total;

    @Schema(description = "当前页", example = "1")
    private int page;

    @Schema(description = "每页条数", example = "10")
    private int size;

    @Schema(description = "总页数", example = "10")
    private long pages;

    public static <T> PageResponse<T> of(IPage<T> iPage) {
        PageResponse<T> resp = new PageResponse<>();
        resp.setList(iPage.getRecords());
        resp.setTotal(iPage.getTotal());
        resp.setPage((int) iPage.getCurrent());
        resp.setSize((int) iPage.getSize());
        resp.setPages(iPage.getPages());
        return resp;
    }

    public static <T> PageResponse<T> of(List<T> list, long total, int page, int size) {
        PageResponse<T> resp = new PageResponse<>();
        resp.setList(list);
        resp.setTotal(total);
        resp.setPage(page);
        resp.setSize(size);
        resp.setPages(size == 0 ? 0 : (long) Math.ceil((double) total / size));
        return resp;
    }
}
