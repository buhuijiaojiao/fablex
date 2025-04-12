package com.github.bhjj.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.bhjj.constant.CacheConsts;
import com.github.bhjj.constant.DatabaseConsts;
import com.github.bhjj.dao.BookInfoMapper;
import com.github.bhjj.dao.HomeBookMapper;
import com.github.bhjj.entity.BookInfo;
import com.github.bhjj.entity.HomeBook;
import com.github.bhjj.resp.Result;
import com.github.bhjj.vo.HomeBookVO;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 首页小说推荐缓存
 *
 * @author ZhangXianDuo
 * @date 2025/4/11
 */
@Component
@RequiredArgsConstructor
public class HomeBookInfoCacheManager {

    private final BookInfoMapper bookInfoMapper;
    private final HomeBookMapper homeBookMapper;

    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.HOME_BOOK_CACHE_NAME)
    public List<HomeBookVO> listHomeBooks() {
        //可以小说推荐表和小说信息表联表查询 但这里先不使用

        //对推荐表查询
        QueryWrapper<HomeBook> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DatabaseConsts.CommonColumnEnum.SORT.getName());
        List<HomeBook> homeBooks = homeBookMapper.selectList(queryWrapper);


        if (!Collections.isEmpty(homeBooks)) {

            //取出推荐的小说id
            List<Long> bookIds = homeBooks.stream()
                    .map(HomeBook::getBookId)
                    .toList();
            //对小说信息表查询
            QueryWrapper<BookInfo> bookInfoQueryWrapper = new QueryWrapper<>();
            bookInfoQueryWrapper.in(DatabaseConsts.CommonColumnEnum.ID.getName(),bookIds);
            List<BookInfo> bookInfos = bookInfoMapper.selectList(bookInfoQueryWrapper);
            //封装，返回
            if (!CollectionUtils.isEmpty(bookInfos)) {
                /*
                * 这段代码实现了 列表到Map的转换 ，
                * 核心目的是将bookInfos列表中的元素按BookInfo对象的唯一ID（Long类型）快速索引，
                * 以便后续快速查找。
                *
                键提取逻辑（BookInfo::getId）
                通过方法引用提取BookInfo的ID作为Map的键，将每个对象关联到唯一标识符，确保查找时间为O(1)。

                值映射优化（Function.identity()）
                使用Function.identity()表示直接保留原始对象作为值。

                优势：避免创建额外的对象，提高性能。
                替代写法：bookInfo -> bookInfo（功能等价，但Function.identity()代码更简洁）
                *
                * */
                Map<Long, BookInfo> bookInfoMap = bookInfos.stream()
                        .collect(Collectors.toMap(BookInfo::getId, Function.identity()));
                return homeBooks.stream().map(v -> {
                    BookInfo bookInfo = bookInfoMap.get(v.getBookId());
                    HomeBookVO homeBookVO = new HomeBookVO();
                    homeBookVO.setType(v.getType());
                    homeBookVO.setBookId(v.getBookId());
                    homeBookVO.setBookName(bookInfo.getBookName());
                    homeBookVO.setPicUrl(bookInfo.getPicUrl());
                    homeBookVO.setAuthorName(bookInfo.getAuthorName());
                    homeBookVO.setBookDesc(bookInfo.getBookDesc());
                    return homeBookVO;
                }).toList();

            }

        }

        return Collections.emptyList();
    }
}
