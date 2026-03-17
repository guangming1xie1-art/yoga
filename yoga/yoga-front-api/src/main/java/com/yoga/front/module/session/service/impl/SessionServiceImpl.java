package com.yoga.front.module.session.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionMapper sessionMapper;

    @Override
    public PageResponse<SessionVO> listSessions(Long venueId, String category,
                                                 String date, PageRequest pageRequest) {
        throw new UnsupportedOperationException("TODO: listSessions");
    }

    @Override
    public SessionVO getDetail(Long id) {
        throw new BusinessException(ResultCode.SESSION_NOT_FOUND);
    }

    @Override
    public PageResponse<SessionVO> search(String keyword, PageRequest pageRequest) {
        Page<SessionVO> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        IPage<SessionVO> resultPage = sessionMapper.searchSessions(page, keyword);
        return PageResponse.build(resultPage);
    }

    @Override
    public PageResponse<?> listReviews(Long sessionId, PageRequest pageRequest) {
        throw new UnsupportedOperationException("TODO: listReviews");
    }
}
