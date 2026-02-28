package com.yoga.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单表
 */
@Data
@TableName("orders")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 课程排期ID */
    private Long sessionId;

    /** 微信支付订单号 */
    private String wxPayOrderNo;

    /** 本系统订单号 */
    private String orderNo;

    /** 实付金额 */
    private BigDecimal amount;

    /**
     * 状态：
     * UNPAID    - 未支付
     * PAID      - 已支付
     * REFUNDING - 退款中
     * REFUNDED  - 已退款
     * CLOSED    - 已关闭
     */
    private String status;

    /**
     * 退款方式：
     * ORIGINAL - 原路退回
     * MANUAL   - 手动退款
     */
    private String refundType;

    /** 退款金额 */
    private BigDecimal refundAmount;

    /** 退款截止时间（课程开始前N小时） */
    private LocalDateTime refundDeadline;

    /** 支付时间 */
    private LocalDateTime paidAt;

    /** 退款时间 */
    private LocalDateTime refundedAt;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
