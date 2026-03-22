package com.yoga.backend.module.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yoga.common.entity.CourseSchedule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ScheduleMapper extends BaseMapper<CourseSchedule> {
}
