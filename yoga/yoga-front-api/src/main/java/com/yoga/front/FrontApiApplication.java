package com.yoga.front;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 瑜伽系统 - 用户端 API 启动类
 */
@SpringBootApplication(scanBasePackages = {"com.yoga.front", "com.yoga.common"})
@MapperScan("com.yoga.front.module.**.mapper")
@EnableScheduling
public class FrontApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FrontApiApplication.class, args);
    }
}
