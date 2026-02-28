package com.yoga.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程排期表（实际开课记录）
 */
@Data
@TableName("course_sessions")
public class CourseSession {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 对应排课规则ID（手动排课可为null） */
    private Long scheduleId;

    /** 课程模板ID */
    private Long templateId;

    /** 教练ID */
    private Long coachId;

    /** 所属场馆ID */
    private Long venueId;

    /** 课程开始时间 */
    private LocalDateTime startTime;

    /** 课程结束时间 */
    private LocalDateTime endTime;

    /** 最大容量 */
    private Integer capacity;

    /** 已预约人数 */
    private Integer bookedCount;

    /** 价格快照（下单时锁价） */
    private BigDecimal price;

    /**
     * 状态：
     * SCHEDULED - 已排期/待开课
     * ONGOING   - 进行中
     * FINISHED  - 已结束
     * CANCELLED - 已取消
     */
    private String status;

    /** 取消原因 */
    private String cancelReason;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
