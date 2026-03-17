package com.yoga.front.module.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * 课程通知服务 - 用于发送课程变更通知
 */
@Service
@RequiredArgsConstructor
public class CourseNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 通知课程排期变更
     * @param sessionId 课程ID
     * @param payload 通知内容
     */
    public void notifySessionChange(Long sessionId, Object payload) {
        messagingTemplate.convertAndSend("/topic/session/" + sessionId, payload);
    }

    /**
     * 向指定用户发送通知
     * @param userId 用户ID
     * @param payload 通知内容
     */
    public void notifyUser(Long userId, Object payload) {
        messagingTemplate.convertAndSendToUser(
                String.valueOf(userId), "/queue/notification", payload);
    }

    /**
     * 通知已预约用户课程变更
     * @param sessionId 课程ID
     * @param bookingIds 预约ID列表
     * @param notification 通知内容
     */
    public void notifyBookingUsers(Long sessionId, java.util.List<Long> bookingIds, Object notification) {
        for (Long bookingId : bookingIds) {
            // 这里可以向特定用户发送通知
            // 实际实现中需要根据bookingId获取userId
            messagingTemplate.convertAndSend("/topic/session/" + sessionId, notification);
        }
    }
}