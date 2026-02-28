package com.yoga.front.module.session.service;

import com.yoga.common.dto.session.SessionVO;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;

public interface SessionService {
    PageResponse<SessionVO> listSessions(Long venueId, String category, String date, PageRequest pageRequest);
    SessionVO getDetail(Long id);
    PageResponse<SessionVO> search(String keyword, PageRequest pageRequest);
    PageResponse<?> listReviews(Long sessionId, PageRequest pageRequest);
}
