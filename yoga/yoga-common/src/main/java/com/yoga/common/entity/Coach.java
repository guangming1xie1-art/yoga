package com.yoga.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 教练表（强绑定 user_id）
 */
@Data
@TableName("coaches")
public class Coach {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 绑定的用户ID */
    private Long userId;

    /** 所属场馆ID */
    private Long venueId;

    /** 真实姓名 */
    private String realName;

    /** 专长/标签，逗号分隔 */
    private String specialty;

    /** 简介 */
    private String bio;

    /** 头像 URL */
    private String avatar;

    /** 从教年限 */
    private Integer yearsExp;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
