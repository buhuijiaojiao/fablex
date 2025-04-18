package com.github.bhjj.service;

import com.github.bhjj.dto.ChapterAddDTO;
import com.github.bhjj.dto.ChapterUpdateDTO;
import com.github.bhjj.dto.PageBean;
import com.github.bhjj.resp.Result;
import com.github.bhjj.vo.*;
import jakarta.validation.Valid;

import java.security.NoSuchAlgorithmException;
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

    /**
     * 章节目录查询
     * @param bookId
     * @return
     */
    Result<List<BookChapterVO>> listChapter(Long bookId);

    /**
     * 获取下一章ID接口
     * @param chapterId
     * @return
     */
    Result<Long> getNextChapterId(Long chapterId);

    /**
     * 获取上一章ID接口
     * @param chapterId
     * @return
     */
    Result<Long> getPreChapterId(Long chapterId);

    /**
     * 增加小说点击量接口
     */
    Result<Void> addVisitCount(Long bookId);

    /**
     * 小说最新章节相关信息查询接口
     * @param bookId
     * @return
     */
    Result<BookChapterAboutVO> getLastChapterAbout(Long bookId);

    /**
     * 小说推荐列表查询接口
     * @param bookId
     * @return
     */
    Result<List<BookInfoVO>> listRecBooks(Long bookId) throws NoSuchAlgorithmException;

    /**
     * 小说最新评论查询接口
     * @param bookId
     * @return
     */
    Result<BookCommentVO> listNewestComments(Long bookId);

    /**
     * 章节管理分页查询
     * @param bookId
     * @param dto
     * @return
     */
    Result<PageVO<BookChapterVO>> listBookChapters(Long bookId, PageBean dto);

    /**
     * 删除章节接口
     * @param chapterId
     * @return
     */
    Result<Void> deleteBookChapter(Long chapterId);

    /**
     * 新建章节接口
     * @return
     */
    Result<Void> addBookChapter(ChapterAddDTO chapterAddDTO);

    /**
     * 根据章节id查询章节信息
     * @param chapterId
     * @return
     */
    Result<ChapterContentVO> getBookChapterById(Long chapterId);

    /**
     * 章节信息更新接口
     * @param chapterId
     * @param chapterUpdateDTO
     * @return
     */
    Result<Void> updateBookChapter(Long chapterId,ChapterUpdateDTO chapterUpdateDTO);
}
