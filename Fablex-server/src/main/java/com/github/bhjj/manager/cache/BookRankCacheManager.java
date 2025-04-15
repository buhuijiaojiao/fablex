package com.github.bhjj.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.bhjj.constant.CacheConsts;
import com.github.bhjj.constant.DatabaseConsts;
import com.github.bhjj.dao.BookInfoMapper;
import com.github.bhjj.entity.BookInfo;
import com.github.bhjj.vo.BookRankVO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhangXianDuo
 * @date 2025/4/15
 */
@Component
@RequiredArgsConstructor
public class BookRankCacheManager {

    private final BookInfoMapper bookInfoMapper;

    /**
     * 查询小说点击榜列表，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER ,
            value = CacheConsts.BOOK_VISIT_RANK_CACHE_NAME)
    public List<BookRankVO> listVisitRankBooks() {
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DatabaseConsts.BookTable.COLUMN_VISIT_COUNT);
        return listRankBooks(queryWrapper);
    }

    /**
     * 查询小说新书榜列表，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.BOOK_NEWEST_RANK_CACHE_NAME)
    public List<BookRankVO> listNewestRankBooks() {
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName());
        return listRankBooks(queryWrapper);
    }

    /**
     * 查询小说更新榜列表，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.BOOK_UPDATE_RANK_CACHE_NAME)
    public List<BookRankVO> listUpdateRankBooks() {
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DatabaseConsts.CommonColumnEnum.UPDATE_TIME.getName());
        return listRankBooks(queryWrapper);
    }

    //抽取
    public List<BookRankVO> listRankBooks(QueryWrapper<BookInfo> queryWrapper) {
        queryWrapper
                //字数为0的小说
                .gt(DatabaseConsts.BookTable.COLUMN_WORD_COUNT, 0)
                //返回最多三十条
                .last(DatabaseConsts.SqlEnum.LIMIT_30.getSql());
        return bookInfoMapper.selectList(queryWrapper)
                .stream()
                .map(v->BookRankVO.builder()
                        .id(v.getId())
                        .categoryId(v.getCategoryId())
                        .categoryName(v.getCategoryName())
                        .picUrl(v.getPicUrl())
                        .bookName(v.getBookName())
                        .authorName(v.getAuthorName())
                        .bookDesc(v.getBookDesc())
                        .wordCount(v.getWordCount())
                        .lastChapterName(v.getLastChapterName())
                        .lastChapterUpdateTime(v.getLastChapterUpdateTime())
                        .build())
                .toList();
    }


}
