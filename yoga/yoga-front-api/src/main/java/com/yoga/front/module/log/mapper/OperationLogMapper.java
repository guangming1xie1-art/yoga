package com.yoga.front.module.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yoga.common.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}