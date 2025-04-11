package com.github.bhjj.service.impl;

import com.github.bhjj.manager.cache.BookCategoryCacheManager;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.BookService;
import com.github.bhjj.vo.BookCategoryVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhangXianDuo
 * @date 2025/4/11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookCategoryCacheManager bookCategoryCacheManager;

    @Override
    public Result<List<BookCategoryVO>> listCategory(Integer workDirection) {
        return Result.success(bookCategoryCacheManager.listCategory(workDirection));
    }
}
