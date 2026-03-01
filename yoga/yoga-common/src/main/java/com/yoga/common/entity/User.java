package com.yoga.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 用户表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("users")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String openid;

    private String unionid;

    private String nickname;

    private String avatarUrl;

    private String phone;

    private Integer gender;

    private Integer disabled;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
