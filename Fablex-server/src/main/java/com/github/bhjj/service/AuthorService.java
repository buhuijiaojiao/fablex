package com.github.bhjj.service;

import com.github.bhjj.dto.AuthorLoginDTO;

/**
 * @author ZhangXianDuo
 * @date 2025/3/9
 */
public interface AuthorService {

    /**
     * 作家登录
     *
     * @param authorLoginDTO
     * @return
     */
    void login(AuthorLoginDTO authorLoginDTO);
}
