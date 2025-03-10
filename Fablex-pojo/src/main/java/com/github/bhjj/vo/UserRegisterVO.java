package com.github.bhjj.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author ZhangXianDuo
 * @date 2025/3/10
 */
@Data
@Builder
public class UserRegisterVO {

    private Long uid;

    private String token;
}