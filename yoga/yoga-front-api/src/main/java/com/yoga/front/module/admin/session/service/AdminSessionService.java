package com.yoga.front.module.admin.session.service;

import com.yoga.common.dto.session.SessionVO;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;

public interface AdminSessionService {
    /**
     * 更新课程状态（如取消、改期等）- 用于管理端
     * @param sessionId 课程ID
     * @param newStatus 新状态
     * @param reason 变更原因
     * @return 更新结果
     */
    boolean updateSessionStatus(Long sessionId, String newStatus, String reason);
    
    /**
     * 获取课程详情
     * @param id 课程ID
     * @return 课程详情
     */
    SessionVO getDetail(Long id);
    
    /**
     * 分页查询课程
     * @param pageRequest 分页参数
     * @return 课程列表
     */
    PageResponse<SessionVO> listSessions(PageRequest pageRequest);
}