package com.github.bhjj.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 跨域配置属性
 *
 * @author ZhangXianDuo
 * @date 2025/3/11
 */
@ConfigurationProperties(prefix = "fablex.cors")
@Data
public class CorsProperties {

    /**
     * 允许跨域的域名
     * */
    private List<String> allowOrigins;
}
