package com.yoga.backend.module.template.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.entity.CourseTemplate;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;

public interface TemplateService {

    PageResponse<CourseTemplate> listTemplates(PageRequest pageRequest);

    CourseTemplate getDetail(Long id);

    CourseTemplate create(CourseTemplate template);

    CourseTemplate update(Long id, CourseTemplate template);

    void delete(Long id);
}
