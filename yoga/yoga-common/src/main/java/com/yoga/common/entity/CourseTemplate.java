package com.yoga.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程模板表
 */
@Data
@TableName("course_templates")
public class CourseTemplate {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long venueId;

    private String name;

    private String category;

    private String description;

    private String coverImage;

    private Integer durationMinutes;

    private Integer capacity;

    private BigDecimal price;

    private String difficulty;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
