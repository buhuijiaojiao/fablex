package com.github.bhjj.auth;

import com.github.bhjj.constant.SystemConfigConsts;
import com.github.bhjj.context.UserHolder;
import com.github.bhjj.dto.UserInfoDTO;
import com.github.bhjj.exception.BusinessException;
import com.github.bhjj.manager.cache.UserInfoCacheManager;
import com.github.bhjj.utils.JwtUtils;
import org.springframework.util.StringUtils;
import com.github.bhjj.enumeration.ErrorCodeEnum;

import java.util.Objects;

/**
 * 策略模式，实现精细化的权限控制
 *
 * @author ZhangXianDuo
 * @date 2025/3/10
 */
public interface AuthStrategy {
    /**
     * 用户认证授权
     *
     * @param token      登录 token
     * @param requestUri 请求的 URI
     * @throws BusinessException 认证失败则抛出业务异常
     */
    void auth(String token, String requestUri) throws BusinessException;

    /**
     * 前台多系统单点登录统一账号认证授权（门户系统、作家系统以及后面会扩展的漫画系统和视频系统等）
     *
     * @param jwtUtils             jwt 工具
     * @param userInfoCacheManager 用户缓存管理对象
     * @param token                token 登录 token
     * @return 用户ID
     */
    default Long authSSO(JwtUtils jwtUtils,
                         String token,
                         UserInfoCacheManager userInfoCacheManager) {
        if (!StringUtils.hasText(token)) {
            // token 为空
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }
        Long userId = jwtUtils.parseToken(token, SystemConfigConsts.FABLEX_FRONT_KEY);
        if (Objects.isNull(userId)) {
            // token 解析失败
            throw new BusinessException(ErrorCodeEnum.USER_LOGIN_EXPIRED);
        }
        UserInfoDTO userInfoDTO = userInfoCacheManager.getUser(userId);
        if (Objects.isNull(userInfoDTO)) {
            // 用户不存在
            throw new BusinessException(ErrorCodeEnum.USER_ACCOUNT_NOT_EXIST);
        }
        // 设置 userId 到当前线程
        UserHolder.setUserId(userId);
        // 返回 userId
        return userId;
    }
}
