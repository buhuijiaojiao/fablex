package com.github.bhjj.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author ZhangXianDuo
 * @date 2025/4/15
 */
@Data
@Builder
public class BookContentAboutVO {
    /**
     * 小说信息
     */
    @Schema(description = "小说信息")
    private BookInfoVO bookInfo;

    /**
     * 章节信息
     */
    @Schema(description = "章节信息")
    private BookChapterVO chapterInfo;

    /**
     * 章节内容
     */
    @Schema(description = "章节内容")
    private String bookContent;
}
