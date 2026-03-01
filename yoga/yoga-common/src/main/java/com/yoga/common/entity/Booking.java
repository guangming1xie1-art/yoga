package com.yoga.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 预约表
 */
@Data
@TableName("bookings")
public class Booking {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long sessionId;

    private Long orderId;

    private String status;

    private String qrCode;

    private LocalDateTime qrExpireAt;

    private String cancelBy;

    private String cancelReason;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
