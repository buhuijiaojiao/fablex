package com.github.bhjj.auth;

import com.github.bhjj.constant.ApiRouterConsts;
import com.github.bhjj.context.UserHolder;
import com.github.bhjj.entity.AuthorInfo;
import com.github.bhjj.enumeration.ErrorCodeEnum;
import com.github.bhjj.exception.BusinessException;
import com.github.bhjj.manager.cache.AuthorInfoCacheManager;
import com.github.bhjj.manager.cache.UserInfoCacheManager;
import com.github.bhjj.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 作家认证策略
 * @author ZhangXianDuo
 * @date 2025/3/10
 */
@Component
@RequiredArgsConstructor
public class AuthorAuthStrategy implements AuthStrategy{

    private final JwtUtils jwtUtils;

    private final UserInfoCacheManager userInfoCacheManager;

    private final AuthorInfoCacheManager authorInfoCacheManager;

    /**
     * 不需要进行作家权限认证的 URI
     */
    private static final List<String> EXCLUDE_URI = List.of(
            ApiRouterConsts.API_AUTHOR_URL_PREFIX + "/register",
            ApiRouterConsts.API_AUTHOR_URL_PREFIX + "/status"
    );

    @Override
    public void auth(String token, String requestUri) throws BusinessException {

        // 统一账号认证
        Long userId = authSSO(jwtUtils, token, userInfoCacheManager);
        if (EXCLUDE_URI.contains(requestUri)) {
            // 该请求不需要进行作家权限认证
            return;
        }
        // 作家权限认证
        AuthorInfo authorInfo = authorInfoCacheManager.getAuthorInfo(userId);
        if (Objects.isNull(authorInfo)) {
            // 作家账号不存在，无权访问作家专区
            throw new BusinessException(ErrorCodeEnum.USER_UN_AUTH);
        }

        // 设置作家ID到当前线程
        UserHolder.setAuthorId(authorInfo.getId());
    }
}
