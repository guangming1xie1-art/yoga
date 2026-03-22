package com.yoga.backend.module.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.entity.CourseTemplate;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.ResultCode;
import com.yoga.backend.module.template.mapper.TemplateMapper;
import com.yoga.backend.module.template.service.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateMapper templateMapper;

    @Override
    public PageResponse<CourseTemplate> listTemplates(PageRequest pageRequest) {
        Page<CourseTemplate> page = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        LambdaQueryWrapper<CourseTemplate> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(CourseTemplate::getCreatedAt);
        Page<CourseTemplate> result = templateMapper.selectPage(page, wrapper);
        return PageResponse.of(result);
    }

    @Override
    public CourseTemplate getDetail(Long id) {
        CourseTemplate template = templateMapper.selectById(id);
        if (template == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "课程模板不存在");
        }
        return template;
    }

    @Override
    public CourseTemplate create(CourseTemplate template) {
        templateMapper.insert(template);
        log.info("创建课程模板成功，模板ID: {}", template.getId());
        return template;
    }

    @Override
    public CourseTemplate update(Long id, CourseTemplate template) {
        CourseTemplate existing = getDetail(id);
        template.setId(id);
        templateMapper.updateById(template);
        log.info("更新课程模板成功，模板ID: {}", id);
        return template;
    }

    @Override
    public void delete(Long id) {
        CourseTemplate template = getDetail(id);
        templateMapper.deleteById(id);
        log.info("删除课程模板成功，模板ID: {}", id);
    }
}
