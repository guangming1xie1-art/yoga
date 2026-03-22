package com.yoga.front.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wx.mini")
public class WxProperties {
    
    private String appId;
    
    private String appSecret;
    
    private String loginUrl;
}
