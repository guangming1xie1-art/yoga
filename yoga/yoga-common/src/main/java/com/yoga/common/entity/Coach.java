package com.yoga.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 教练表
 */
@Data
@TableName("coaches")
public class Coach {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long venueId;

    private String realName;

    private String specialty;

    private String bio;

    private String avatar;

    private Integer yearsExp;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
