package com.github.bhjj.service.impl;

import com.github.bhjj.entity.BookChapter;
import com.github.bhjj.manager.cache.*;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.BookService;
import com.github.bhjj.vo.*;
import com.github.bhjj.vo.BookContentAboutVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
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

    private final BookRankCacheManager bookRankCacheManager;

    private final BookInfoCacheManager bookInfoCacheManager;

    private final BookChapterCacheManager bookChapterCacheManager;

    private final BookContentCacheManager bookContentCacheManager;

    /**
     *  小说列表查询接口
     * @param workDirection
     * @return
     */
    @Override
    public Result<List<BookCategoryVO>> listCategory(Integer workDirection) {
        return Result.success(bookCategoryCacheManager.listCategory(workDirection));
    }

    /**
     *小说点击榜接口
     *
     * @return
     */
    @Override
    public Result<List<BookRankVO>> listVisitRankBooks() {
        return Result.success(bookRankCacheManager.listVisitRankBooks());
    }
    /**
     * 新书榜接口
     */
    @Override
    public Result<List<BookRankVO>> listNewestRankBooks() {
        return Result.success(bookRankCacheManager.listNewestRankBooks());
    }

    /**
     * 更新榜接口
     * @return
     */
    @Override
    public Result<List<BookRankVO>> listUpdateRankBooks() {
        return Result.success(bookRankCacheManager.listUpdateRankBooks());
    }

    /**
     *根据id小说查询接口
     * @param bookId
     * @return
     */
    @Override
    public Result<BookInfoVO> getBookById(Long bookId) {
        return Result.success(bookInfoCacheManager.getBookById(bookId));
    }

    /**
     * 根据章节id查询小说信息接口
     * @param chapterId
     * @return
     */
    @Override
    public Result<BookContentAboutVO> getBookContentById(Long chapterId) {
        BookChapterVO bookChapterVO = bookChapterCacheManager.getChapter(chapterId);

        String bookContent = bookContentCacheManager.getBookContentByChapterId(chapterId);

        BookInfoVO bookInfoVO = bookInfoCacheManager.getBookById(bookChapterVO.getBookId());

        return Result.success(
                BookContentAboutVO.builder()
                        .bookInfo(bookInfoVO)
                        .chapterInfo(bookChapterVO)
                        .bookContent(bookContent)
                        .build()
        );
    }
}
