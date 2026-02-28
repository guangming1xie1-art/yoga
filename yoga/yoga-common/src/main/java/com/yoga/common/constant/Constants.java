package com.yoga.common.constant;

/**
 * 系统常量
 */
public final class Constants {

    private Constants() {}

    // ---- JWT ----
    public static final String TOKEN_PREFIX        = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String CLAIM_USER_ID       = "userId";
    public static final String CLAIM_USERNAME      = "username";
    public static final String CLAIM_ROLES         = "roles";
    public static final String CLAIM_VENUE_ID      = "venueId";

    // ---- Redis Key 前缀 ----
    public static final String REDIS_TOKEN_BLACKLIST  = "yoga:token:blacklist:";
    public static final String REDIS_REFRESH_TOKEN    = "yoga:token:refresh:";
    public static final String REDIS_WX_SESSION       = "yoga:wx:session:";
    public static final String REDIS_CAPTCHA          = "yoga:captcha:";
    public static final String REDIS_SESSION_SEATS    = "yoga:session:seats:";

    // ---- 角色 ----
    public static final String ROLE_SYS_ADMIN    = "SYS_ADMIN";
    public static final String ROLE_VENUE_ADMIN  = "VENUE_ADMIN";
    public static final String ROLE_COACH        = "COACH";
    public static final String ROLE_USER         = "USER";

    // ---- 业务常量 ----
    public static final int    DEFAULT_ADVANCE_DAYS   = 7;   // 默认提前可预约天数
    public static final int    DEFAULT_REFUND_HOURS   = 24;  // 默认退款截止（课前小时）
    public static final String QR_CODE_PREFIX         = "yoga:qr:";
    public static final int    QR_EXPIRE_MINUTES      = 30;  // 签到二维码有效期（分钟）

    // ---- 分页默认值 ----
    public static final int PAGE_DEFAULT_SIZE         = 10;
    public static final int PAGE_MAX_SIZE             = 100;
}
