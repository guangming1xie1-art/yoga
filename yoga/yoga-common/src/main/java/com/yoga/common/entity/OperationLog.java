package com.yoga.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 操作日志表
 */
@Data
@TableName("operation_logs")
public class OperationLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 操作人ID */
    private Long operatorId;

    /** 操作人姓名/账号 */
    private String operatorName;

    /** 操作模块 */
    private String module;

    /** 操作类型：CREATE/UPDATE/DELETE/QUERY/EXPORT/LOGIN */
    private String action;

    /** 操作描述 */
    private String description;

    /** 请求 IP */
    private String ip;

    /** 请求方法 */
    private String httpMethod;

    /** 请求路径 */
    private String requestUri;

    /** 请求参数（JSON） */
    private String requestParams;

    /** 响应码 */
    private Integer responseCode;

    /** 耗时（ms） */
    private Long costMs;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
