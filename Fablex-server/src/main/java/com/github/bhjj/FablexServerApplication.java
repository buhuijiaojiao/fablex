package com.github.bhjj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author ZhangXianDuo
 * @Date 2025/3/8
 */
@SpringBootApplication
//@EnableCaching
public class FablexServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FablexServerApplication.class, args);
    }
}
