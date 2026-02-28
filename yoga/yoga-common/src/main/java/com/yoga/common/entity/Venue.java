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

    /** 场馆名称 */
    private String name;

    /** 城市 */
    private String city;

    /** 详细地址 */
    private String address;

    /** 联系电话 */
    private String phone;

    /** 营业时间描述 */
    private String businessHours;

    /** 封面图 URL */
    private String coverImage;

    /** 简介 */
    private String description;

    /** 状态：0-禁用 1-启用 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
