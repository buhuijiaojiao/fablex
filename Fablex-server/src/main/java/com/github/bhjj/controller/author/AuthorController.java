package com.github.bhjj.controller.author;

import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.github.bhjj.constant.ApiRouterConsts;
import com.github.bhjj.context.UserHolder;
import com.github.bhjj.dto.AuthorRegisterDTO;
import com.github.bhjj.dto.PageBean;
import com.github.bhjj.entity.BookInfo;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.AuthorService;
import com.github.bhjj.service.BookService;
import com.github.bhjj.vo.BookChapterVO;
import com.github.bhjj.vo.BookInfoVO;
import com.github.bhjj.vo.PageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;

/**
 * 作家API控制器
 * @author ZhangXianDuo
 * @date 2025/4/6
 */
@RestController
@Tag(name = "AuthorController", description = "作家后台-作者模块")
@Slf4j
@RequestMapping(ApiRouterConsts.API_AUTHOR_URL_PREFIX)
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    private final BookService bookService;
    /**
     * 作家注册接口
     */
    @Operation(summary = "作家注册接口")
    @PostMapping("register")
    public Result<Void> register(@Valid @RequestBody AuthorRegisterDTO authorRegisterDTO) {
        log.info("作家注册请求参数: {}", authorRegisterDTO);
        authorRegisterDTO.setUserId(UserHolder.getUserId());
        return authorService.register(authorRegisterDTO);
    }

    /**
     * 作家状态接口
     * @return
     */
    @Operation(summary = "作家状态接口")
    @GetMapping("status")
    public Result<Integer> getStatus() {
        log.info("作家状态查询");
        return authorService.getStatus(UserHolder.getUserId());
    }

    /**
     * 名下小说查询接口
     * @param dto
     * @return
     */
    @Operation(summary = "名下小说查询接口")
    @GetMapping("books")
    public Result<PageVO<BookInfoVO>> listBooks(@ParameterObject PageBean dto){
        return authorService.listBooks(dto);
    }

    /**
     * 章节管理接口(分页查询)
     * @param bookId
     * @param dto
     * @return
     */
    @Operation(summary = "章节管理接口")
    @GetMapping("/book/chapters/{bookId}")
    public Result<PageVO<BookChapterVO>> listBookChapters(
            @Parameter(description = "小说ID") @PathVariable("bookId") Long bookId ,
            @ParameterObject PageBean dto) {
        return bookService.listBookChapters(bookId, dto);
    }
    /**
     * 小说章节删除接口
     */
    @Operation(summary = "小说章节删除接口")
    @DeleteMapping("book/chapter/{chapterId}")
    public Result<Void> deleteBookChapter(
            @Parameter(description = "章节ID") @PathVariable("chapterId") Long chapterId) {
        return bookService.deleteBookChapter(chapterId);
    }


}
