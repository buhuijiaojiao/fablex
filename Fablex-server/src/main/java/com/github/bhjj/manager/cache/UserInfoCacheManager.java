package com.github.bhjj.manager.cache;

import com.github.bhjj.constant.CacheConsts;
import com.github.bhjj.dao.UserInfoMapper;
import com.github.bhjj.dto.UserInfoDTO;
import com.github.bhjj.entity.UserInfo;
import com.github.bhjj.manager.redis.VerifyCodeManager;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 用户信息缓存管理器
 * @author ZhangXianDuo
 * @date 2025/3/10
 */
@Component
@RequiredArgsConstructor
public class UserInfoCacheManager {
    private final UserInfoMapper userInfoMapper;

    /**
     * 查询用户信息，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
            value = CacheConsts.USER_INFO_CACHE_NAME)
    public UserInfoDTO getUser(Long userId) {
        UserInfo userInfo = userInfoMapper.selectById(userId);
        if (Objects.isNull(userInfo)) {
            return null;
        }
        return UserInfoDTO.builder()
                .id(userInfo.getId())
                .status(userInfo.getStatus()).build();
    }

}
