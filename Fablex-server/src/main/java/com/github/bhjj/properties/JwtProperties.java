package com.github.bhjj.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author ZhangXianDuo
 * @date 2025/3/9
 */
@ConfigurationProperties(prefix = "fablex.jwt")
@Component
@Getter
@Setter
public class JwtProperties {
    private String secret;
    private Long expireTime;
    private String header;
}
