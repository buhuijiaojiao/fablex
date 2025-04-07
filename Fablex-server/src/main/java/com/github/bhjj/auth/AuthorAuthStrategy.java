package com.github.bhjj.auth;

import com.github.bhjj.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 作家认证策略
 * @author ZhangXianDuo
 * @date 2025/3/10
 */
@Component
@RequiredArgsConstructor
public class AuthorAuthStrategy implements AuthStrategy{

    @Override
    public void auth(String token, String requestUri) throws BusinessException {

    }
}
