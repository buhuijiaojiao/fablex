package com.github.bhjj.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.bhjj.manager.redis.VerifyCodeManager;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.ResourceService;
import com.github.bhjj.vo.ImgVerifyCodeVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 资源服务实现类
 * @author ZhangXianDuo
 * @date 2025/3/11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final VerifyCodeManager verifyCodeManager;

    /**
     * 获取图片验证码
     *
     * @return Base64编码的图片
     */
    @Override
    public Result<ImgVerifyCodeVO> getImgVerifyCode() throws IOException{
        //验证码的唯一标识
        String sessionId = IdWorker.get32UUID();
        return Result.success(ImgVerifyCodeVO.builder()
                .sessionId(sessionId)
                .img(verifyCodeManager.genImgVerifyCode(sessionId))
                .build());
    }
}
