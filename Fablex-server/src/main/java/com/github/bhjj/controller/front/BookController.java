package com.github.bhjj.controller.front;

import com.github.bhjj.constant.ApiRouterConsts;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.BookService;
import com.github.bhjj.vo.BookCategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
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
     * @param workDirection
     * @return
     */
    @Operation(summary = "小说列表查询接口")
    @GetMapping("category/list")
    public Result<List<BookCategoryVO>> listCategory(
            @Parameter(description = "作品方向", required = true) Integer workDirection) {
        log.info("小说列表查询");
        return bookService.listCategory(workDirection);
    }

}
