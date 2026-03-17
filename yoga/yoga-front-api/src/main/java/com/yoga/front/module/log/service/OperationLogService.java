package com.yoga.front.module.log.service;

import com.yoga.common.entity.OperationLog;

public interface OperationLogService {
    /**
     * 保存操作日志
     * @param operationLog 操作日志实体
     */
    void saveLog(OperationLog operationLog);
}