package com.github.bhjj.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ZhangXianDuo
 * @date 2025/3/9
 */
@ConfigurationProperties(prefix = "fablex.jwt")
@Component
@Data
public class JwtProperties {
    private String secret;
    private Long expire;
}
