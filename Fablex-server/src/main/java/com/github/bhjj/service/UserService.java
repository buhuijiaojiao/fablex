package com.github.bhjj.service;

import com.github.bhjj.dto.UserRegisterDTO;
import com.github.bhjj.resp.Result;
import com.github.bhjj.vo.UserRegisterVO;
import jakarta.validation.Valid;

/**
 * @author ZhangXianDuo
 * @date 2025/3/10
 */
public interface UserService {
    /**
     * 用户注册
     * @param userRegisterDTO
     * @return
     */
    Result<UserRegisterVO> register(UserRegisterDTO userRegisterDTO);
}
