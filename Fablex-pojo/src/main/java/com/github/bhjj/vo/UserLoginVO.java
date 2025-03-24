package com.github.bhjj.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author ZhangXianDuo
 * @date 2025/3/24
 */
@Data
@Builder
public class UserLoginVO {
    @Schema(description = "用户ID")
    private Long uid;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户token")
    private String token;
}
