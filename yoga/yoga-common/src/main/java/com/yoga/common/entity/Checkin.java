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

    private Long bookingId;

    private Long userId;

    private Long sessionId;

    private String method;

    private LocalDateTime checkinTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
