package com.github.bhjj.service;

import com.github.bhjj.resp.Result;
import com.github.bhjj.vo.BookCategoryVO;

import java.util.List;

/**
 * @author ZhangXianDuo
 * @date 2025/4/11
 */
public interface BookService {
    /**
     *  小说列表查询接口
     * @param workDirection
     * @return
     */
    Result<List<BookCategoryVO>> listCategory(Integer workDirection);
}
