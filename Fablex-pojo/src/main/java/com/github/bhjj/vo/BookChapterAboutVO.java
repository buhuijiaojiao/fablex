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
public class BookChapterAboutVO {
    private BookChapterVO chapterInfo;

    /**
     * 章节总数
     */
    @Schema(description = "章节总数")
    private Long chapterTotal;

    /**
     * 内容概要（30字）
     */
    @Schema(description = " 内容概要（30字）")
    private String contentSummary;
}
