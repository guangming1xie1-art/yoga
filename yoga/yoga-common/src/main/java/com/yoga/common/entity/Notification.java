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

    /** 接收用户ID（null 表示广播全体） */
    private Long userId;

    /** 通知类型：SYSTEM / BOOKING / ORDER / REVIEW / BROADCAST */
    private String type;

    /** 标题 */
    private String title;

    /** 内容 */
    private String content;

    /** 关联业务ID（如 bookingId/orderId） */
    private Long relatedId;

    /** 是否已读：0-未读 1-已读 */
    private Integer isRead;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
