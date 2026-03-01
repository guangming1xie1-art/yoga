package com.yoga.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 通知表
 */
@Data
@TableName("notifications")
public class Notification {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String type;

    private String title;

    private String content;

    private Long relatedId;

    private Integer isRead;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
