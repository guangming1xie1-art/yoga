package com.yoga.front.module.session.service.impl;

import com.yoga.common.dto.session.SessionVO;
import com.yoga.common.exception.BusinessException;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.common.result.ResultCode;
import com.yoga.front.module.session.mapper.SessionMapper;
import com.yoga.front.module.session.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionMapper sessionMapper;

    @Override
    public PageResponse<SessionVO> listSessions(Long venueId, String category,
                                                 String date, PageRequest pageRequest) {
        // TODO: 根据条件查询 course_sessions JOIN course_templates/coaches/venues
        throw new UnsupportedOperationException("TODO: listSessions");
    }

    @Override
    public SessionVO getDetail(Long id) {
        // TODO: 查询排期详情 + 教练/场馆信息
        throw new BusinessException(ResultCode.SESSION_NOT_FOUND);
    }

    @Override
    public PageResponse<SessionVO> search(String keyword, PageRequest pageRequest) {
        // TODO: 全文搜索
        throw new UnsupportedOperationException("TODO: search");
    }

    @Override
    public PageResponse<?> listReviews(Long sessionId, PageRequest pageRequest) {
        // TODO: 查询评价列表
        throw new UnsupportedOperationException("TODO: listReviews");
    }
}
