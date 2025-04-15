package com.github.bhjj.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.bhjj.constant.CacheConsts;
import com.github.bhjj.constant.DatabaseConsts;
import com.github.bhjj.dao.BookCategoryMapper;
import com.github.bhjj.dao.BookInfoMapper;
import com.github.bhjj.entity.BookCategory;
import com.github.bhjj.entity.BookInfo;
import com.github.bhjj.resp.Result;
import com.github.bhjj.vo.BookCategoryVO;
import com.github.bhjj.vo.BookRankVO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ZhangXianDuo
 * @date 2025/4/11
 */
@RequiredArgsConstructor
@Component
public class BookCategoryCacheManager {

    private final BookCategoryMapper bookCategoryMapper;




    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
                value = CacheConsts.BOOK_CATEGORY_LIST_CACHE_NAME)
    public List<BookCategoryVO> listCategory(Integer workDirection) {
        QueryWrapper<BookCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookCategoryTable.COLUMN_WORK_DIRECTION, workDirection);
        return bookCategoryMapper.selectList(queryWrapper).stream().map(bookCategory ->
                BookCategoryVO.builder()
                        .id(bookCategory.getId())
                        .name(bookCategory.getName())
                        .build()).toList();
    }

}
