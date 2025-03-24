package com.github.bhjj.service;

import com.github.bhjj.dto.UserLoginDTO;
import com.github.bhjj.dto.UserRegisterDTO;
import com.github.bhjj.resp.Result;
import com.github.bhjj.vo.UserLoginVO;
import com.github.bhjj.vo.UserRegisterVO;
import jakarta.validation.Valid;

/**
 * 用户信息服务
 *
 * @author ZhangXianDuo
 * @date 2025/3/10
 */
public interface UserService {
    /**
     * 用户注册
     *
     * @param userRegisterDTO
     * @return
     */
    Result<UserRegisterVO> register(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @return
     */
    Result<UserLoginVO> login(UserLoginDTO userLoginDTO);
}
