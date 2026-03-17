package com.yoga.front.module.log.service.impl;

import com.yoga.common.entity.OperationLog;
import com.yoga.front.module.log.mapper.OperationLogMapper;
import com.yoga.front.module.log.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl implements OperationLogService {
    
    private final OperationLogMapper operationLogMapper;

    @Override
    public void saveLog(OperationLog operationLog) {
        operationLogMapper.insert(operationLog);
    }
}