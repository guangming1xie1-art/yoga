package com.yoga.common.exception;

import com.yoga.common.result.ResultCode;

/**
 * 业务异常（受控异常，直接映射到 Result 错误响应）
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.INTERNAL_ERROR.getCode();
    }

    public int getCode() { return code; }
}
