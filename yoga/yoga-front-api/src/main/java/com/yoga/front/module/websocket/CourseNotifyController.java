package com.yoga.front.module.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * WebSocket - 课程变更实时推送端点
 */
@Controller
@RequiredArgsConstructor
public class CourseNotifyController {

    @MessageMapping("/ping")
    public void ping() {
        // 用于测试WebSocket连接
    }
}
