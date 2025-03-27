package com.github.bhjj.service;

import com.github.bhjj.dto.BookSearchDTO;
import com.github.bhjj.resp.Result;
import com.github.bhjj.vo.BookInfoVO;
import com.github.bhjj.vo.PageVO;

/**
 *  搜索服务
 * @author ZhangXianDuo
 * @date 2025/3/27
 */
public interface SearchService {

    /**
     * 小说搜索
     *
     * @param condition 搜索条件
     * @return 搜索结果
     */
    Result<PageVO<BookInfoVO>> searchBooks(BookSearchDTO condition);
}
