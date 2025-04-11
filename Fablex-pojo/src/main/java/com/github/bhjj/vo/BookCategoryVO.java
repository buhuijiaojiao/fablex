package com.github.bhjj.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 小说分类
 *
 * @author ZhangXianDuo
 * @date 2025/4/11
 */
@Data
@Builder
public class BookCategoryVO {

    /**
     * 类别ID
     */
    @Schema(description = "类别ID")
    private Long id;

    /**
     * 类别名
     */
    @Schema(description = "类别名")
    private String name;


}
