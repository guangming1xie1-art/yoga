package com.yoga.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.Instant;

/**
 * 统一响应体 Result<T>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "统一响应体")
public class Result<T> implements Serializable {

    @Schema(description = "业务状态码，0=成功", example = "0")
    private int code;

    @Schema(description = "提示信息", example = "OK")
    private String message;

    @Schema(description = "业务数据")
    private T data;

    @Schema(description = "响应时间戳(UTC)")
    private Instant timestamp;

    private Result() {}

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now();
    }

    // ---- 工厂方法 ----

    public static <T> Result<T> ok() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> ok(T data, String message) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(ResultCode.INTERNAL_ERROR.getCode(), message, null);
    }

    // ---- Getters ----

    public int getCode()        { return code; }
    public String getMessage()  { return message; }
    public T getData()          { return data; }
    public Instant getTimestamp(){ return timestamp; }

    public boolean isSuccess()  { return this.code == ResultCode.SUCCESS.getCode(); }
}
