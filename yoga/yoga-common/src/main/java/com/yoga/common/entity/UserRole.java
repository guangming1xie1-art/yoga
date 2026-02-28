package com.yoga.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户-角色关联表（含 venue_id 作用域）
 */
@Data
@TableName("user_roles")
public class UserRole {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long roleId;

    /** 作用域：所属场馆ID（SYS_ADMIN 时为 null） */
    private Long venueId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
