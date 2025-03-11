package com.github.bhjj.service;

import com.github.bhjj.resp.Result;
import com.github.bhjj.vo.ImgVerifyCodeVO;

import java.io.IOException;

/**
 * @author ZhangXianDuo
 * @date 2025/3/11
 */
public interface ResourceService {
    /**
     * 获取图片验证码
     *
     * @throws IOException 验证码图片生成失败
     * @return Base64编码的图片
     */
    Result<ImgVerifyCodeVO> getImgVerifyCode() throws IOException;
}
