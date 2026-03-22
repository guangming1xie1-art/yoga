package com.yoga.backend.module.schedule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.entity.CourseSchedule;
import com.yoga.common.entity.CourseSession;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.ResultCode;
import com.yoga.backend.module.schedule.mapper.ScheduleMapper;
import com.yoga.backend.module.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleMapper scheduleMapper;
    private final SessionMapper sessionMapper;

    @Override
    public PageResponse<CourseSchedule> listSchedules(PageRequest pageRequest) {
        Page<CourseSchedule> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        LambdaQueryWrapper<CourseSchedule> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(CourseSchedule::getCreatedAt);
        Page<CourseSchedule> result = scheduleMapper.selectPage(page, wrapper);
        return PageResponse.of(result);
    }

    @Override
    public CourseSchedule getDetail(Long id) {
        CourseSchedule schedule = scheduleMapper.selectById(id);
        if (schedule == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "排课规则不存在");
        }
        return schedule;
    }

    @Override
    public CourseSchedule create(CourseSchedule schedule) {
        scheduleMapper.insert(schedule);
        log.info("创建排课规则成功，规则ID: {}", schedule.getId());
        return schedule;
    }

    @Override
    public CourseSchedule update(Long id, CourseSchedule schedule) {
        CourseSchedule existing = getDetail(id);
        schedule.setId(id);
        scheduleMapper.updateById(schedule);
        log.info("更新排课规则成功，规则ID: {}", id);
        return schedule;
    }

    @Override
    public void delete(Long id) {
        CourseSchedule schedule = getDetail(id);
        scheduleMapper.deleteById(id);
        log.info("删除排课规则成功，规则ID: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateSessions(Long scheduleId) {
        CourseSchedule schedule = getDetail(scheduleId);

        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusDays(schedule.getAdvanceDays());

        List<CourseSession> sessions = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            String weekdayStr = dayOfWeek.toString();

            if (schedule.getWeekdays().contains(weekdayStr)) {
                CourseSession session = new CourseSession();
                session.setScheduleId(scheduleId);
                session.setTemplateId(schedule.getTemplateId());
                session.setCoachId(schedule.getCoachId());
                session.setVenueId(getVenueIdByTemplate(schedule.getTemplateId()));
                session.setStartTime(LocalDateTime.of(currentDate, schedule.getStartTime()));
                session.setEndTime(LocalDateTime.of(currentDate, schedule.getEndTime()));
                session.setCapacity(getCapacityByTemplate(schedule.getTemplateId()));
                session.setBookedCount(0);
                session.setPrice(getPriceByTemplate(schedule.getTemplateId()));
                session.setStatus("SCHEDULED");
                sessions.add(session);
            }
            currentDate = currentDate.plusDays(1);
        }

        if (!sessions.isEmpty()) {
            sessionMapper.insertBatch(sessions);
            schedule.setLastGenerated(LocalDateTime.now());
            scheduleMapper.updateById(schedule);
            log.info("生成课程排期成功，规则ID: {}, 生成数量: {}", scheduleId, sessions.size());
        }
    }

    private Long getVenueIdByTemplate(Long templateId) {
        return 1L;
    }

    private Integer getCapacityByTemplate(Long templateId) {
        return 20;
    }

    private java.math.BigDecimal getPriceByTemplate(Long templateId) {
        return new java.math.BigDecimal("99.00");
    }
}
