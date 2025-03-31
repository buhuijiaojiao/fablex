package com.github.bhjj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.Logbook;

import static org.zalando.logbook.core.Conditions.*;
import static org.zalando.logbook.core.Conditions.contentType;

/**
 * Logbook 配置
 *
 * @author ZhangXianDuo
 * @date 2025/3/31
 */
@Configuration
public class LogbookConfig {

    @Bean
    public Logbook logbook() {
        return Logbook.builder()
                .condition(exclude(
                        // 忽略 OPTIONS 请求
                        requestWithMethod("OPTIONS"),
                        // 忽略 /actuator 以及其子路径（Spring Boot Actuator 提供的端点）的请求
                        requestTo("/actuator/**"),
                        // 忽略 Swagger 文档路径
                        requestTo("/swagger-ui/**"),
                        requestTo("/v3/api-docs/**"),
                        // 忽略二进制文件请求
                        contentType("application/octet-stream"),
                        // 忽略文件上传请求
                        contentType("multipart/form-data")
                ))
                .build();
    }

}
