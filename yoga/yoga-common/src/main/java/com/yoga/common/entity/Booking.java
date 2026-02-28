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

    /** 用户ID */
    private Long userId;

    /** 课程排期ID */
    private Long sessionId;

    /** 关联订单ID */
    private Long orderId;

    /**
     * 状态：
     * PENDING   - 待支付
     * CONFIRMED - 已确认
     * CANCELLED - 已取消
     * CHECKED_IN- 已签到
     */
    private String status;

    /** 签到二维码内容（唯一） */
    private String qrCode;

    /** 二维码过期时间 */
    private LocalDateTime qrExpireAt;

    /** 取消人：user/admin/system */
    private String cancelBy;

    /** 取消原因 */
    private String cancelReason;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
