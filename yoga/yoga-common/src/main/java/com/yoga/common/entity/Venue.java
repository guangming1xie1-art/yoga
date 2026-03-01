package com.yoga.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 场馆表
 */
@Data
@TableName("venues")
public class Venue {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String city;

    private String address;

    private String phone;

    private String businessHours;

    private String coverImage;

    private String description;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
