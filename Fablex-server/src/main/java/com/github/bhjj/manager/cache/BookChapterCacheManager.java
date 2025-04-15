package com.github.bhjj.manager.cache;

import com.github.bhjj.constant.CacheConsts;
import com.github.bhjj.dao.BookChapterMapper;
import com.github.bhjj.entity.BookChapter;
import com.github.bhjj.vo.BookChapterVO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author ZhangXianDuo
 * @date 2025/4/15
 */
@Component
@RequiredArgsConstructor
public class BookChapterCacheManager {

    private final BookChapterMapper bookChapterMapper;

    /**
     * 查询小说章节信息，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER ,
            value = CacheConsts.BOOK_CHAPTER_CACHE_NAME)
    public BookChapterVO getChapter(Long id) {
        BookChapter bookChapter = bookChapterMapper.selectById(id);
        return BookChapterVO.builder()
                .id(bookChapter.getId())
                .bookId(bookChapter.getBookId())
                .chapterNum(bookChapter.getChapterNum())
                .chapterName(bookChapter.getChapterName())
                .chapterWordCount(bookChapter.getWordCount())
                .chapterUpdateTime(bookChapter.getUpdateTime())
                .isVip(bookChapter.getIsVip())
                .build();
    }
}
