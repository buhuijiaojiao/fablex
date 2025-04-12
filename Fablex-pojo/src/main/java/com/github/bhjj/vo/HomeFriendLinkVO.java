package com.github.bhjj.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 *首页友情链接VO
 *
 * @author ZhangXianDuo
 * @date 2025/4/12
 */
@Data
public class HomeFriendLinkVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 链接名
     */
    @Schema(description = "链接名")
    private String linkName;

    /**
     * 链接url
     */
    @Schema(description = "链接url")
    private String linkUrl;
}
