package com.yoga.front.module.session.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yoga.common.entity.CourseSession;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SessionMapper extends BaseMapper<CourseSession> {
    // 复杂 SQL 可在 resources/mapper/SessionMapper.xml 中定义
}
