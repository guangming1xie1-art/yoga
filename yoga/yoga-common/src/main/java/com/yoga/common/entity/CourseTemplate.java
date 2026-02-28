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

    /** 所属场馆ID */
    private Long venueId;

    /** 课程名称 */
    private String name;

    /** 课程类别（瑜伽类型） */
    private String category;

    /** 课程描述 */
    private String description;

    /** 封面图 */
    private String coverImage;

    /** 课程时长（分钟） */
    private Integer durationMinutes;

    /** 最大容量 */
    private Integer capacity;

    /** 课程定价 */
    private BigDecimal price;

    /** 难度：BEGINNER/INTERMEDIATE/ADVANCED */
    private String difficulty;

    /** 状态：0-下线 1-上线 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
