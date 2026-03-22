package com.yoga.backend.module.schedule.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.entity.CourseSchedule;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;

public interface ScheduleService {

    PageResponse<CourseSchedule> listSchedules(PageRequest pageRequest);

    CourseSchedule getDetail(Long id);

    CourseSchedule create(CourseSchedule schedule);

    CourseSchedule update(Long id, CourseSchedule schedule);

    void delete(Long id);

    void generateSessions(Long scheduleId);
}
