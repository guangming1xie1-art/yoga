package com.yoga.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 周期排课规则表
 */
@Data
@TableName("course_schedules")
public class CourseSchedule {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 课程模板ID */
    private Long templateId;

    /** 教练ID */
    private Long coachId;

    /** 上课星期（1=周一…7=周日，逗号分隔，如 "1,3,5"） */
    private String weekdays;

    /** 开始时间 */
    private LocalTime startTime;

    /** 结束时间 */
    private LocalTime endTime;

    /** 提前可预约天数 */
    private Integer advanceDays;

    /** 最后一次生成排期的日期（用于增量生成） */
    private LocalDateTime lastGenerated;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
