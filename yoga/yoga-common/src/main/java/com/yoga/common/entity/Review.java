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

    /** 用户ID */
    private Long userId;

    /** 课程排期ID */
    private Long sessionId;

    /** 教练ID */
    private Long coachId;

    /** 预约ID */
    private Long bookingId;

    /** 评分：1-5 */
    private Integer rating;

    /** 评价内容 */
    private String content;

    /** 是否公开显示：0-隐藏 1-显示 */
    private Integer isVisible;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
