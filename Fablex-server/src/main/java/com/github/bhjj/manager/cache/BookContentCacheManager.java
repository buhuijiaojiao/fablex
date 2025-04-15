package com.github.bhjj.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.bhjj.constant.CacheConsts;
import com.github.bhjj.constant.DatabaseConsts;
import com.github.bhjj.dao.BookContentMapper;
import com.github.bhjj.entity.BookContent;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @author ZhangXianDuo
 * @date 2025/4/15
 */
@Component
@RequiredArgsConstructor
public class BookContentCacheManager {

    private final BookContentMapper bookContentMapper;

    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
            value = CacheConsts.BOOK_CONTENT_CACHE_NAME)
    public String getBookContentByChapterId(Long chapterId) {
        QueryWrapper<BookContent> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq(DatabaseConsts.BookContentTable.COLUMN_CHAPTER_ID, chapterId)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        BookContent bookContent = bookContentMapper.selectOne(queryWrapper);
        return bookContent.getContent();
    }
}
