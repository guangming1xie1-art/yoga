package com.yoga.common.dto.booking;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用于Excel导出的预约记录DTO
 */
@Data
@ColumnWidth(20) // 设置默认列宽
public class BookingExportDTO {
    @ExcelProperty("预约编号")
    private Long id;

    @ExcelProperty("课程名称")
    private String sessionName;

    @ExcelProperty("教练姓名")
    private String coachName;

    @ExcelProperty("场馆名称")
    private String venueName;

    @ExcelProperty("课程开始时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sessionStartTime;

    @ExcelProperty("课程结束时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sessionEndTime;

    @ExcelProperty("价格")
    private BigDecimal price;

    @ExcelProperty("预约状态")
    private String status;

    @ExcelProperty("预约时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}