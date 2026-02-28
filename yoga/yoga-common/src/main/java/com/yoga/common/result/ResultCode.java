package com.yoga.common.result;

/**
 * 业务错误码枚举 (ErrorCode)
 * 命名规范：HTTP风格大类 + 四位业务码
 */
public enum ResultCode {

    // ---- 通用成功 ----
    SUCCESS(0, "操作成功"),

    // ---- 通用客户端错误 4xxx ----
    BAD_REQUEST(4000, "请求参数错误"),
    UNAUTHORIZED(4001, "未登录或Token已过期"),
    FORBIDDEN(4003, "无权限执行此操作"),
    NOT_FOUND(4004, "资源不存在"),
    METHOD_NOT_ALLOWED(4005, "请求方法不允许"),
    DUPLICATE(4006, "数据已存在"),
    VALIDATION_ERROR(4007, "参数校验失败"),
    TOKEN_EXPIRED(4008, "Token已过期"),
    TOKEN_INVALID(4009, "Token无效"),

    // ---- 业务错误 5xxx ----
    VENUE_NOT_FOUND(5001, "场馆不存在"),
    COACH_NOT_FOUND(5002, "教练不存在"),
    SESSION_NOT_FOUND(5003, "课程排期不存在"),
    SESSION_FULL(5004, "课程名额已满"),
    SESSION_CANCELLED(5005, "课程已取消"),
    BOOKING_NOT_FOUND(5006, "预约记录不存在"),
    BOOKING_ALREADY_EXISTS(5007, "您已预约该课程"),
    BOOKING_CANNOT_CANCEL(5008, "预约无法取消"),
    ORDER_NOT_FOUND(5009, "订单不存在"),
    ORDER_PAY_FAILED(5010, "支付失败"),
    ORDER_REFUND_FAILED(5011, "退款失败"),
    REVIEW_ALREADY_EXISTS(5012, "已提交过评价"),
    CHECKIN_ALREADY(5013, "该课程已签到"),
    CHECKIN_QR_EXPIRED(5014, "签到二维码已过期"),
    USER_DISABLED(5015, "用户已被禁用"),
    USER_NOT_FOUND(5016, "用户不存在"),
    WX_LOGIN_FAILED(5017, "微信登录失败"),
    SCHEDULE_GENERATE_ERROR(5018, "排课生成失败"),

    // ---- 系统错误 9xxx ----
    INTERNAL_ERROR(9000, "系统内部错误"),
    SERVICE_UNAVAILABLE(9001, "服务不可用"),
    DB_ERROR(9002, "数据库操作异常");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode()     { return code; }
    public String getMessage(){ return message; }
}
