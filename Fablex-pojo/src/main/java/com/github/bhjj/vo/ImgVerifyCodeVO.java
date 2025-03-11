package com.github.bhjj.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 图片验证码返回对象
 * @author ZhangXianDuo
 * @date 2025/3/11
 */
@Data
@Builder
public class ImgVerifyCodeVO {
    /**
     * 当前会话ID，用于标识改图形验证码属于哪个会话
     * */
    @Schema(description = "sessionId")
    private String sessionId;

    /**
     * Base64 编码的验证码图片
     * */
    @Schema(description = "Base64 编码的验证码图片")
    private String img;
}
