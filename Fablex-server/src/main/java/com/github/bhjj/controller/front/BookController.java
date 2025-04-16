package com.github.bhjj.controller.front;

import com.github.bhjj.constant.ApiRouterConsts;
import com.github.bhjj.dto.VisitAddDTO;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.BookService;
import com.github.bhjj.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 前台门户-小说模块API控制器
 *
 * @author ZhangXianDuo
 * @date 2025/4/11
 */
@Tag(name = "BookController", description = "前台门户-小说模块")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(ApiRouterConsts.API_FRONT_BOOK_URL_PREFIX)
public class BookController {

    private final BookService bookService;

    /**
     * 小说列表查询接口
     *
     * @param workDirection
     * @return
     */
    @Operation(summary = "小说列表查询接口")
    @GetMapping("category/list")
    public Result<List<BookCategoryVO>> listCategory(
            @Parameter(description = "作品方向", required = true) Integer workDirection) {
        return bookService.listCategory(workDirection);
    }

    /**
     * 根据id小说查询接口
     *
     * @param bookId
     * @return
     */
    @Operation(summary = "根据小说id查询小说信息接口")
    @GetMapping("{id}")
    public Result<BookInfoVO> getBookById(
            @Parameter(description = "小说 ID") @PathVariable("id") Long bookId) {
        return bookService.getBookById(bookId);
    }

    /**
     * 根据章节id查询小说信息接口
     * @param chapterId
     * @return
     */
    @Operation(summary = "根据章节id查询小说信息接口")
    @GetMapping("content/{chapterId}")
    public Result<BookContentAboutVO> getBookContentAboutById(
            @Parameter(description = "章节 ID") @PathVariable("chapterId") Long chapterId) {
        return bookService.getBookContentById(chapterId);
    }

    /**
     * 章节目录接口
     * @param bookId
     * @return
     */
    @Operation(summary = "章节目录接口")
    @GetMapping("chapter/list")
    public Result<List<BookChapterVO>> listChapter(
            @Parameter (description = "小说id") @RequestParam("bookId") Long bookId) {
        return bookService.listChapter(bookId);
    }

    /**
     * 获取下一章ID接口
     * @param chapterId
     * @return
     */
    @Operation(summary = "获取下一章ID接口")
    @GetMapping("next_chapter_id/{chapter_id}")
    public Result<Long> getNextChapterId(
            @Parameter(description = "章节 ID") @PathVariable("chapter_id") Long chapterId) {
        return bookService.getNextChapterId(chapterId);
    }

    /**
     * 获取上一章ID接口
     * @param chapterId
     * @return
     */
    @Operation(summary = "获取上一章ID接口")
    @GetMapping("pre_chapter_id/{chapter_id}")
    public Result<Long> getPreChapterId(
            @Parameter(description = "章节 ID") @PathVariable("chapter_id") Long chapterId) {
        return bookService.getPreChapterId(chapterId);
    }

    /**
     * 增加小说点击量接口
     */
    @Operation(summary = "增加小说点击量接口")
    @PostMapping("visit")
    public Result<Void> addVisitCount(@RequestBody VisitAddDTO visitAddDTO) {
        return bookService.addVisitCount(visitAddDTO.getBookId());
    }
    /**
     * 小说点击榜接口
     *
     * @return
     */
    @Operation(summary = "访问榜单")
    @GetMapping("visit_rank")
    public Result<List<BookRankVO>> listVisitRankBooks() {
        return bookService.listVisitRankBooks();
    }

    /**
     * 新书榜接口
     */
    @Operation(summary = "新书榜单")
    @GetMapping("newest_rank")
    public Result<List<BookRankVO>> listNewestRankBooks() {
        return bookService.listNewestRankBooks();
    }

    /**
     * 更新榜单接口
     *
     * @return
     */
    @Operation(summary = "更新榜单")
    @GetMapping("update_rank")
    public Result<List<BookRankVO>> listUpdateRankBooks() {
        return bookService.listUpdateRankBooks();
    }

    /**
     * 小说最新章节相关信息查询接口
     * @param bookId
     * @return
     */
    @Operation(summary = "小说最新章节相关信息查询接口")
    @GetMapping("last_chapter/about")
    public Result<BookChapterAboutVO> getLastChapterAbout(@Parameter(description = "小说 ID") Long bookId){
        return bookService.getLastChapterAbout(bookId);
    }

    /**
     * 小说推荐列表查询接口
     * @param bookId
     * @return
     */
    @Operation(summary = "小说推荐列表查询接口")
    @GetMapping("rec_list")
    public Result<List<BookInfoVO>> listRecBooks(@Parameter(description = "小说 ID") Long bookId) throws NoSuchAlgorithmException {
        return bookService.listRecBooks(bookId);
    }

    /**
     * 小说最新评论查询接口
     */
    @Operation(summary = "小说最新评论查询接口")
    @GetMapping("comment/newest_list")
    public Result<BookCommentVO> listNewestComments(
            @Parameter(description = "小说ID") Long bookId) {
        //TODO 待办
        return bookService.listNewestComments(bookId);
    }
}
