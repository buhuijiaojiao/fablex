package com.github.bhjj.properties;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@Data
public class JwtProperties {
    private String secret;
    private Long expire;
}
