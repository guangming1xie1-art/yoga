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

    private Long templateId;

    private Long coachId;

    private String weekdays;

    private LocalTime startTime;

    private LocalTime endTime;

    private Integer advanceDays;

    private LocalDateTime lastGenerated;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
