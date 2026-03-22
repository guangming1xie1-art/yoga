package com.yoga.backend.module.coach.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yoga.common.entity.Coach;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;

public interface CoachService {

    PageResponse<Coach> listCoaches(PageRequest pageRequest);

    Coach getDetail(Long id);

    Coach create(Coach coach);

    Coach update(Long id, Coach coach);

    void delete(Long id);
}
