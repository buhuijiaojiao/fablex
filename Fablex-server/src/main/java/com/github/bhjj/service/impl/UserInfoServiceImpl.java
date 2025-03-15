package com.github.bhjj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.bhjj.constant.DatabaseConsts;
import com.github.bhjj.constant.SystemConfigConsts;
import com.github.bhjj.dao.UserInfoMapper;
import com.github.bhjj.dto.UserRegisterDTO;
import com.github.bhjj.entity.UserInfo;
import com.github.bhjj.enumeration.ErrorCodeEnum;
import com.github.bhjj.exception.BusinessException;
import com.github.bhjj.manager.cache.UserInfoCacheManager;
import com.github.bhjj.manager.redis.VerifyCodeManager;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.UserInfoService;
import com.github.bhjj.utils.JwtUtils;
import com.github.bhjj.vo.UserRegisterVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


/**
 * 用户Service实现类
 *
 * @author ZhangXianDuo
 * @date 2025/3/10
 */
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final UserInfoMapper userInfoMapper;
    private final UserInfoCacheManager userInfoCacheManager;
    private final VerifyCodeManager verifyCodeManager;
    private final JwtUtils jwtUtils;

    /**
     * 用户注册
     *
     * @param userRegisterDTO
     * @return
     */
    @Override
    public Result<UserRegisterVO> register(UserRegisterDTO userRegisterDTO) {
        if (!verifyCodeManager.imgVerifyCodeOk(userRegisterDTO.getSessionId(), userRegisterDTO.getVelCode())) {
            throw new BusinessException(ErrorCodeEnum.USER_VERIFY_CODE_ERROR);
        }
        //校验手机号是否已经被注册
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.UserInfoTable.COLUMN_USERNAME, userRegisterDTO.getUsername())
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        if (userInfoMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException(ErrorCodeEnum.USER_NAME_EXIST);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(
                DigestUtils.md5DigestAsHex(userRegisterDTO.getPassword().getBytes(StandardCharsets.UTF_8)));
        userInfo.setUsername(userRegisterDTO.getUsername());
        userInfo.setNickName(userRegisterDTO.getUsername());
        userInfo.setCreateTime(LocalDateTime.now());
        userInfo.setUpdateTime(LocalDateTime.now());
        userInfo.setSalt("0");
        userInfoMapper.insert(userInfo);
        verifyCodeManager.removeImgVerifyCode(userRegisterDTO.getSessionId());
        // 生成token返回数据
        return Result.success(UserRegisterVO.builder()
                .token(jwtUtils.generateToken(userInfo.getId(), SystemConfigConsts.FABLEX_FRONT_KEY))
                .uid(userInfo.getId())
                .build());
    }
}
