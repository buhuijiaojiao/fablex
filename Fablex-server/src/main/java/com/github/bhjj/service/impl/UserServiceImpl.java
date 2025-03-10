package com.github.bhjj.service.impl;

import com.github.bhjj.dto.UserRegisterDTO;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.UserService;
import com.github.bhjj.vo.UserRegisterVO;
import org.springframework.stereotype.Service;


/**
 * 用户Service实现类
 * @author ZhangXianDuo
 * @date 2025/3/10
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public Result<UserRegisterVO> register(UserRegisterDTO userRegisterDTO) {
        // TODO: 2025/3/10 注册用户
        return null;
    }
}
