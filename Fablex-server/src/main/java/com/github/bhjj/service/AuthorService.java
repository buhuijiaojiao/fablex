package com.github.bhjj.service;

import com.github.bhjj.dto.AuthorRegisterDTO;
import com.github.bhjj.resp.Result;
import jakarta.validation.Valid;

/**
 *  作家服务
 * @author ZhangXianDuo
 * @date 2025/4/1
 */
public interface AuthorService {
    Result<Void> register(AuthorRegisterDTO authorRegisterDTO);

    Result<Integer> getStatus(Long userId);
}
