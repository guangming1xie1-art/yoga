package com.yoga.front.module.session.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yoga.common.entity.CourseSession;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SessionMapper extends BaseMapper<CourseSession> {
}
