package com.github.bhjj.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置接口文档
 *
 * @author ZhangXianDuo
 * @date 2025/3/9
 */

@Configuration
public class SpringDocConfig {
    /**
     * 作家端
     * @return
     */
    @Bean
    public OpenAPI authorApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("作家端API")
                        .description("去测试吧")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("bhjj")
                                .email("zxd13781369670@gmail.com")));
    }
}