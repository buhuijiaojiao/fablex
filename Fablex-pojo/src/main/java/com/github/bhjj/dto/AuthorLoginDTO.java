package com.github.bhjj.dto;

import lombok.Getter;

/**
 * @author ZhangXianDuo
 * @date 2025/3/9
 */
@Getter
public class AuthorLoginDTO {
    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
