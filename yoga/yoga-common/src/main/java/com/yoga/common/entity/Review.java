package com.yoga.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评价表
 */
@Data
@TableName("reviews")
public class Review {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long sessionId;

    private Long coachId;

    private Long bookingId;

    private Integer rating;

    private String content;

    private Integer isVisible;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
