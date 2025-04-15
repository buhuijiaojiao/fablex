package com.github.bhjj.service;

import com.github.bhjj.resp.Result;
import com.github.bhjj.vo.BookCategoryVO;
import com.github.bhjj.vo.BookContentAboutVO;
import com.github.bhjj.vo.BookInfoVO;
import com.github.bhjj.vo.BookRankVO;

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

    /**
     *小说点击榜接口
     *
     * @return
     */
    Result<List<BookRankVO>> listVisitRankBooks();

    /**
     * 新书榜接口
     */
    Result<List<BookRankVO>> listNewestRankBooks();

    /**
     * 更新榜接口
     * @return
     */
    Result<List<BookRankVO>> listUpdateRankBooks();

    /**
     *根据id小说查询接口
     * @param bookId
     * @return
     */
    Result<BookInfoVO> getBookById(Long bookId);

    /**
     * 根据章节id查询小说信息接口
     * @param chapterId
     * @return
     */
    Result<BookContentAboutVO> getBookContentById(Long chapterId);
}
