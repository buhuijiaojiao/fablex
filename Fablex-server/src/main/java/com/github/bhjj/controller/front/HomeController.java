package com.github.bhjj.controller.front;

import com.github.bhjj.constant.ApiRouterConsts;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.HomeService;
import com.github.bhjj.vo.HomeBookVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZhangXianDuo
 * @date 2025/4/11
 */
@Tag(name = "HomeController", description = "前台门户-首页模块")
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_HOME_URL_PREFIX)
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final HomeService homeService;

    @Operation(summary = "首页小说推荐查询接口")
    @GetMapping("books")
    public Result<List<HomeBookVO>> listHomeBooks() {

        //测试虚拟线程处理请求
        log.debug("处理请求的线程：{}", Thread.currentThread());
        return homeService.listHomeBooks();
    }
}
