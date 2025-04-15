package com.github.bhjj.controller.front;

import com.github.bhjj.constant.ApiRouterConsts;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.BookService;
import com.github.bhjj.vo.BookCategoryVO;
import com.github.bhjj.vo.BookContentAboutVO;
import com.github.bhjj.vo.BookInfoVO;
import com.github.bhjj.vo.BookRankVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
