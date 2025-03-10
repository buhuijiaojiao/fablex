package com.github.bhjj.auth;

import com.github.bhjj.exception.BusinessException;
import com.github.bhjj.manager.cache.UserInfoCacheManager;
import com.github.bhjj.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 前台用户认证策略
 * @author ZhangXianDuo
 * @date 2025/3/10
 */
@Component
@RequiredArgsConstructor
public class FrontAuthStrategy implements AuthStrategy{
    private final JwtUtils jwtUtils;

    private final UserInfoCacheManager userInfoCacheManager;
    @Override
    public void auth(String token, String requestUri) throws BusinessException {
        authSSO(jwtUtils, token, userInfoCacheManager);
    }
}
