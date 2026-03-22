package com.yoga.backend.module.coach.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.entity.Coach;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.ResultCode;
import com.yoga.backend.module.coach.mapper.CoachMapper;
import com.yoga.backend.module.coach.service.CoachService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoachServiceImpl implements CoachService {

    private final CoachMapper coachMapper;

    @Override
    public PageResponse<Coach> listCoaches(PageRequest pageRequest) {
        Page<Coach> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        LambdaQueryWrapper<Coach> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Coach::getCreatedAt);
        Page<Coach> result = coachMapper.selectPage(page, wrapper);
        return PageResponse.of(result);
    }

    @Override
    public Coach getDetail(Long id) {
        Coach coach = coachMapper.selectById(id);
        if (coach == null) {
            throw new BusinessException(ResultCode.COACH_NOT_FOUND);
        }
        return coach;
    }

    @Override
    public Coach create(Coach coach) {
        coachMapper.insert(coach);
        log.info("创建教练成功，教练ID: {}", coach.getId());
        return coach;
    }

    @Override
    public Coach update(Long id, Coach coach) {
        Coach existing = getDetail(id);
        coach.setId(id);
        coachMapper.updateById(coach);
        log.info("更新教练成功，教练ID: {}", id);
        return coach;
    }

    @Override
    public void delete(Long id) {
        Coach coach = getDetail(id);
        coachMapper.deleteById(id);
        log.info("删除教练成功，教练ID: {}", id);
    }
}
