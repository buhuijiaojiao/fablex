package com.github.bhjj.service;

import com.github.bhjj.dto.UserRegisterDTO;
import com.github.bhjj.resp.Result;
import com.github.bhjj.vo.UserRegisterVO;

/**
 * 用户信息服务
 *
 * @author ZhangXianDuo
 * @date 2025/3/10
 */
public interface UserInfoService {
    /**
     * 用户注册
     * @param userRegisterDTO
     * @return
     */
    Result<UserRegisterVO> register(UserRegisterDTO userRegisterDTO);
}
