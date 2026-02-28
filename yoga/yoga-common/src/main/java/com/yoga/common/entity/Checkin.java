package com.yoga.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 签到表
 */
@Data
@TableName("checkins")
public class Checkin {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 预约ID */
    private Long bookingId;

    /** 用户ID */
    private Long userId;

    /** 课程排期ID */
    private Long sessionId;

    /** 签到方式：QR_CODE / MANUAL（管理员补录） */
    private String method;

    /** 签到时间 */
    private LocalDateTime checkinTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
