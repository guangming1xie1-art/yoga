package com.yoga.front.module.admin.session.service.impl;

import com.yoga.common.dto.session.SessionVO;
import com.yoga.common.entity.CourseSession;
import com.yoga.common.page.PageRequest;
import com.yoga.common.page.PageResponse;
import com.yoga.front.module.admin.session.service.AdminSessionService;
import com.yoga.front.module.session.mapper.SessionMapper;
import com.yoga.front.module.websocket.CourseNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminSessionServiceImpl implements AdminSessionService {
    
    private final SessionMapper sessionMapper;
    private final CourseNotificationService courseNotificationService;

    @Override
    public boolean updateSessionStatus(Long sessionId, String newStatus, String reason) {
        try {
            // 查询课程信息
            CourseSession courseSession = sessionMapper.selectById(sessionId);
            if (courseSession == null) {
                log.warn("课程不存在，ID: {}", sessionId);
                return false;
            }

            // 保存原始状态用于通知
            String oldStatus = courseSession.getStatus();
            
            // 更新课程状态和取消原因
            courseSession.setStatus(newStatus);
            if (reason != null) {
                courseSession.setCancelReason(reason);
            }
            int result = sessionMapper.updateById(courseSession);
            
            if (result > 0) {
                // 通知已预约的用户课程状态变更
                notifyUsersOfSessionChange(sessionId, oldStatus, newStatus, reason);
                log.info("成功更新课程状态，ID: {}, 从 {} 变更为 {}", sessionId, oldStatus, newStatus);
                return true;
            } else {
                log.warn("更新课程状态失败，ID: {}", sessionId);
                return false;
            }
        } catch (Exception e) {
            log.error("更新课程状态时发生异常，ID: {}", sessionId, e);
            return false;
        }
    }

    @Override
    public SessionVO getDetail(Long id) {
        throw new UnsupportedOperationException("暂未实现");
    }

    @Override
    public PageResponse<SessionVO> listSessions(PageRequest pageRequest) {
        throw new UnsupportedOperationException("暂未实现");
    }

    /**
     * 通知已预约的用户课程状态变更
     */
    private void notifyUsersOfSessionChange(Long sessionId, String oldStatus, String newStatus, String reason) {
        // 创建通知消息
        var notification = java.util.Map.of(
            "sessionId", sessionId,
            "oldStatus", oldStatus,
            "newStatus", newStatus,
            "reason", reason,
            "timestamp", java.time.LocalDateTime.now()
        );

        // 发送WebSocket通知到课程相关的主题
        courseNotificationService.notifySessionChange(sessionId, notification);
        log.info("已发送课程状态变更通知，课程ID: {}", sessionId);
    }
}