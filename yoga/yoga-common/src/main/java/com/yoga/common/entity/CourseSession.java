package com.yoga.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程排期表
 */
@Data
@TableName("course_sessions")
public class CourseSession {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long scheduleId;

    private Long templateId;

    private Long coachId;

    private Long venueId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer capacity;

    private Integer bookedCount;

    private BigDecimal price;

    private String status;

    private String cancelReason;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
