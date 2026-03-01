package com.yoga.front.module.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * WebSocket - 课程变更实时推送
 */
@Controller
@RequiredArgsConstructor
public class CourseNotifyController {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifySessionChange(Long sessionId, Object payload) {
        messagingTemplate.convertAndSend("/topic/session/" + sessionId, payload);
    }

    public void notifyUser(Long userId, Object payload) {
        messagingTemplate.convertAndSendToUser(
                String.valueOf(userId), "/queue/notification", payload);
    }

    @MessageMapping("/ping")
    public void ping() {
    }
}
