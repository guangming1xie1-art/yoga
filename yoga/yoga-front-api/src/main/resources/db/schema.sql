-- ============================================================
-- 瑜伽场馆课程预约系统 - 数据库初始化 SQL
-- 数据库版本：MySQL 8.0+
-- 字符集：utf8mb4
-- 时区：Asia/Shanghai
-- ============================================================

CREATE DATABASE IF NOT EXISTS yoga_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE yoga_db;

-- ============================================================
-- 1. 用户表 users
-- ============================================================
CREATE TABLE IF NOT EXISTS `users` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `openid`      VARCHAR(64)  DEFAULT NULL COMMENT '微信OpenID',
  `unionid`     VARCHAR(64)  DEFAULT NULL COMMENT '微信UnionID',
  `nickname`    VARCHAR(64)  DEFAULT NULL COMMENT '昵称',
  `avatar_url`  VARCHAR(512) DEFAULT NULL COMMENT '头像URL',
  `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '绑定手机号',
  `gender`      TINYINT      DEFAULT 0   COMMENT '性别：0-未知 1-男 2-女',
  `disabled`    TINYINT      DEFAULT 0   COMMENT '是否禁用：0-正常 1-禁用',
  `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`     TINYINT      DEFAULT 0   COMMENT '逻辑删除：0-正常 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================================
-- 2. 角色表 roles
-- ============================================================
CREATE TABLE IF NOT EXISTS `roles` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `code`        VARCHAR(32)  NOT NULL COMMENT '角色编码',
  `name`        VARCHAR(64)  NOT NULL COMMENT '角色名称',
  `description` VARCHAR(256) DEFAULT NULL COMMENT '角色描述',
  `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 初始化角色数据
INSERT IGNORE INTO `roles` (`code`, `name`, `description`) VALUES
('SYS_ADMIN',   '系统管理员', '拥有所有权限，管理场馆、用户等'),
('VENUE_ADMIN',  '场馆管理员', '管理本场馆课程、教练、预约等'),
('COACH',        '教练',       '教练账号，查看自己课程'),
('USER',         '普通用户',   '用户端小程序用户');

-- ============================================================
-- 3. 用户角色关联表 user_roles（含 venue_id 作用域）
-- ============================================================
CREATE TABLE IF NOT EXISTS `user_roles` (
  `id`         BIGINT   NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id`    BIGINT   NOT NULL COMMENT '用户ID',
  `role_id`    BIGINT   NOT NULL COMMENT '角色ID',
  `venue_id`   BIGINT   DEFAULT NULL COMMENT '作用域场馆ID（SYS_ADMIN时为null）',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_venue_id` (`venue_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ============================================================
-- 4. 场馆表 venues
-- ============================================================
CREATE TABLE IF NOT EXISTS `venues` (
  `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '场馆ID',
  `name`           VARCHAR(128) NOT NULL COMMENT '场馆名称',
  `city`           VARCHAR(64)  NOT NULL COMMENT '城市',
  `address`        VARCHAR(256) DEFAULT NULL COMMENT '详细地址',
  `phone`          VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
  `business_hours` VARCHAR(128) DEFAULT NULL COMMENT '营业时间',
  `cover_image`    VARCHAR(512) DEFAULT NULL COMMENT '封面图URL',
  `description`    TEXT         DEFAULT NULL COMMENT '场馆简介',
  `status`         TINYINT      DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `created_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`        TINYINT      DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_city` (`city`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场馆表';

-- ============================================================
-- 5. 教练表 coaches（强绑定 user_id）
-- ============================================================
CREATE TABLE IF NOT EXISTS `coaches` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '教练ID',
  `user_id`     BIGINT       NOT NULL COMMENT '绑定用户ID',
  `venue_id`    BIGINT       NOT NULL COMMENT '所属场馆ID',
  `real_name`   VARCHAR(64)  NOT NULL COMMENT '真实姓名',
  `specialty`   VARCHAR(256) DEFAULT NULL COMMENT '专长/标签（逗号分隔）',
  `bio`         TEXT         DEFAULT NULL COMMENT '个人简介',
  `avatar`      VARCHAR(512) DEFAULT NULL COMMENT '头像URL',
  `years_exp`   INT          DEFAULT 0   COMMENT '从教年限',
  `status`      TINYINT      DEFAULT 1   COMMENT '状态：0-禁用 1-启用',
  `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`     TINYINT      DEFAULT 0   COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_venue_id` (`venue_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教练表';

-- ============================================================
-- 6. 课程模板表 course_templates
-- ============================================================
CREATE TABLE IF NOT EXISTS `course_templates` (
  `id`               BIGINT         NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `venue_id`         BIGINT         NOT NULL COMMENT '所属场馆ID',
  `name`             VARCHAR(128)   NOT NULL COMMENT '课程名称',
  `category`         VARCHAR(64)    DEFAULT NULL COMMENT '课程类别',
  `description`      TEXT           DEFAULT NULL COMMENT '课程描述',
  `cover_image`      VARCHAR(512)   DEFAULT NULL COMMENT '封面图URL',
  `duration_minutes` INT            NOT NULL DEFAULT 60 COMMENT '时长（分钟）',
  `capacity`         INT            NOT NULL DEFAULT 20 COMMENT '最大容量',
  `price`            DECIMAL(10,2)  NOT NULL DEFAULT 0.00 COMMENT '定价',
  `difficulty`       VARCHAR(32)    DEFAULT 'BEGINNER' COMMENT '难度',
  `status`           TINYINT        DEFAULT 1 COMMENT '状态：0-下线 1-上线',
  `created_at`       DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`       DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`          TINYINT        DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_venue_id` (`venue_id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程模板表';

-- ============================================================
-- 7. 排课规则表 course_schedules（周期排课）
-- ============================================================
CREATE TABLE IF NOT EXISTS `course_schedules` (
  `id`             BIGINT      NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `template_id`    BIGINT      NOT NULL COMMENT '课程模板ID',
  `coach_id`       BIGINT      NOT NULL COMMENT '教练ID',
  `weekdays`       VARCHAR(32) NOT NULL COMMENT '上课星期（1-7逗号分隔）',
  `start_time`     TIME        NOT NULL COMMENT '开始时间',
  `end_time`       TIME        NOT NULL COMMENT '结束时间',
  `advance_days`   INT         NOT NULL DEFAULT 7  COMMENT '提前可预约天数',
  `last_generated` DATETIME    DEFAULT NULL COMMENT '最后生成排期时间',
  `status`         TINYINT     DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `created_at`     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`        TINYINT     DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_template_id` (`template_id`),
  KEY `idx_coach_id` (`coach_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='排课规则表';

-- ============================================================
-- 8. 课程排期表 course_sessions（实际开课记录）
-- ============================================================
CREATE TABLE IF NOT EXISTS `course_sessions` (
  `id`            BIGINT        NOT NULL AUTO_INCREMENT COMMENT '排期ID',
  `schedule_id`   BIGINT        DEFAULT NULL COMMENT '排课规则ID（手动排课可为null）',
  `template_id`   BIGINT        NOT NULL COMMENT '课程模板ID',
  `coach_id`      BIGINT        NOT NULL COMMENT '教练ID',
  `venue_id`      BIGINT        NOT NULL COMMENT '场馆ID',
  `start_time`    DATETIME      NOT NULL COMMENT '开始时间',
  `end_time`      DATETIME      NOT NULL COMMENT '结束时间',
  `capacity`      INT           NOT NULL DEFAULT 20 COMMENT '最大容量',
  `booked_count`  INT           NOT NULL DEFAULT 0  COMMENT '已预约人数',
  `price`         DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '价格快照',
  `status`        VARCHAR(16)   NOT NULL DEFAULT 'SCHEDULED' COMMENT '状态',
  `cancel_reason` VARCHAR(256)  DEFAULT NULL COMMENT '取消原因',
  `created_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`       TINYINT       DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  KEY `idx_template_id` (`template_id`),
  KEY `idx_coach_id` (`coach_id`),
  KEY `idx_venue_id` (`venue_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程排期表';

-- ============================================================
-- 9. 预约表 bookings
-- ============================================================
CREATE TABLE IF NOT EXISTS `bookings` (
  `id`            BIGINT      NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `user_id`       BIGINT      NOT NULL COMMENT '用户ID',
  `session_id`    BIGINT      NOT NULL COMMENT '课程排期ID',
  `order_id`      BIGINT      DEFAULT NULL COMMENT '关联订单ID',
  `status`        VARCHAR(16) NOT NULL DEFAULT 'PENDING' COMMENT '状态',
  `qr_code`       VARCHAR(128) DEFAULT NULL COMMENT '签到二维码内容（唯一）',
  `qr_expire_at`  DATETIME    DEFAULT NULL COMMENT '二维码过期时间',
  `cancel_by`     VARCHAR(16) DEFAULT NULL COMMENT '取消人：user/admin/system',
  `cancel_reason` VARCHAR(256) DEFAULT NULL COMMENT '取消原因',
  `created_at`    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`       TINYINT     DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_session` (`user_id`, `session_id`, `deleted`),
  UNIQUE KEY `uk_qr_code` (`qr_code`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_session_id` (`session_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约表';

-- ============================================================
-- 10. 订单表 orders
-- ============================================================
CREATE TABLE IF NOT EXISTS `orders` (
  `id`              BIGINT        NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `user_id`         BIGINT        NOT NULL COMMENT '用户ID',
  `session_id`      BIGINT        NOT NULL COMMENT '课程排期ID',
  `order_no`        VARCHAR(64)   NOT NULL COMMENT '本系统订单号',
  `wx_pay_order_no` VARCHAR(64)   DEFAULT NULL COMMENT '微信支付订单号',
  `amount`          DECIMAL(10,2) NOT NULL COMMENT '实付金额',
  `status`          VARCHAR(16)   NOT NULL DEFAULT 'UNPAID' COMMENT '状态',
  `refund_type`     VARCHAR(16)   DEFAULT NULL COMMENT '退款方式：ORIGINAL/MANUAL',
  `refund_amount`   DECIMAL(10,2) DEFAULT NULL COMMENT '退款金额',
  `refund_deadline` DATETIME      DEFAULT NULL COMMENT '退款截止时间',
  `paid_at`         DATETIME      DEFAULT NULL COMMENT '支付时间',
  `refunded_at`     DATETIME      DEFAULT NULL COMMENT '退款时间',
  `created_at`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`         TINYINT       DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_session_id` (`session_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ============================================================
-- 11. 签到表 checkins
-- ============================================================
CREATE TABLE IF NOT EXISTS `checkins` (
  `id`           BIGINT      NOT NULL AUTO_INCREMENT COMMENT '签到ID',
  `booking_id`   BIGINT      NOT NULL COMMENT '预约ID',
  `user_id`      BIGINT      NOT NULL COMMENT '用户ID',
  `session_id`   BIGINT      NOT NULL COMMENT '课程排期ID',
  `method`       VARCHAR(16) NOT NULL DEFAULT 'QR_CODE' COMMENT '签到方式：QR_CODE/MANUAL',
  `checkin_time` DATETIME    NOT NULL COMMENT '签到时间',
  `created_at`   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_booking_id` (`booking_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_session_id` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='签到表';

-- ============================================================
-- 12. 评价表 reviews
-- ============================================================
CREATE TABLE IF NOT EXISTS `reviews` (
  `id`         BIGINT   NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `user_id`    BIGINT   NOT NULL COMMENT '用户ID',
  `session_id` BIGINT   NOT NULL COMMENT '课程排期ID',
  `coach_id`   BIGINT   NOT NULL COMMENT '教练ID',
  `booking_id` BIGINT   NOT NULL COMMENT '预约ID',
  `rating`     TINYINT  NOT NULL COMMENT '评分：1-5',
  `content`    TEXT     DEFAULT NULL COMMENT '评价内容',
  `is_visible` TINYINT  DEFAULT 1 COMMENT '是否公开：0-隐藏 1-显示',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted`    TINYINT  DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_booking_id` (`booking_id`),
  KEY `idx_session_id` (`session_id`),
  KEY `idx_coach_id` (`coach_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- ============================================================
-- 13. 通知表 notifications
-- ============================================================
CREATE TABLE IF NOT EXISTS `notifications` (
  `id`         BIGINT       NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `user_id`    BIGINT       DEFAULT NULL COMMENT '接收用户ID（null=广播）',
  `type`       VARCHAR(32)  NOT NULL COMMENT '类型：SYSTEM/BOOKING/ORDER/REVIEW/BROADCAST',
  `title`      VARCHAR(128) NOT NULL COMMENT '标题',
  `content`    TEXT         NOT NULL COMMENT '内容',
  `related_id` BIGINT       DEFAULT NULL COMMENT '关联业务ID',
  `is_read`    TINYINT      DEFAULT 0 COMMENT '是否已读：0-未读 1-已读',
  `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

-- ============================================================
-- 14. 操作日志表 operation_logs
-- ============================================================
CREATE TABLE IF NOT EXISTS `operation_logs` (
  `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `operator_id`     BIGINT       DEFAULT NULL COMMENT '操作人ID',
  `operator_name`   VARCHAR(64)  DEFAULT NULL COMMENT '操作人账号/姓名',
  `module`          VARCHAR(64)  DEFAULT NULL COMMENT '操作模块',
  `action`          VARCHAR(32)  DEFAULT NULL COMMENT '操作类型',
  `description`     VARCHAR(256) DEFAULT NULL COMMENT '操作描述',
  `ip`              VARCHAR(64)  DEFAULT NULL COMMENT '请求IP',
  `http_method`     VARCHAR(16)  DEFAULT NULL COMMENT 'HTTP方法',
  `request_uri`     VARCHAR(512) DEFAULT NULL COMMENT '请求URI',
  `request_params`  TEXT         DEFAULT NULL COMMENT '请求参数（JSON）',
  `response_code`   INT          DEFAULT NULL COMMENT '响应码',
  `cost_ms`         BIGINT       DEFAULT NULL COMMENT '耗时（ms）',
  `created_at`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_operator_id` (`operator_id`),
  KEY `idx_module` (`module`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';
