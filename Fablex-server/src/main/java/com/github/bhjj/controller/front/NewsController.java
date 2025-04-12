package com.github.bhjj.controller.front;

import com.github.bhjj.constant.ApiRouterConsts;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.NewsService;
import com.github.bhjj.vo.NewsInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台门户-新闻模块
 *
 * @author ZhangXianDuo
 * @date 2025/4/12
 */
@Tag(name = "NewsController", description = "前台门户-新闻模块")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(ApiRouterConsts.API_FRONT_NEWS_URL_PREFIX)

public class NewsController {

    private final NewsService newsService;

    /**
     * 最新新闻列表查询接口
     *
     * @return
     */
    @Operation(summary = "最新新闻列表查询接口")
    @GetMapping("latest_list")
    public Result<List<NewsInfoVO>> listLatestNews() {
        return newsService.listLatestNews();
    }

    /**
     * 根据id查询新闻内容接口
     *
     * @param id
     * @return
     */
    @Operation(summary = "根据id查询新闻内容接口")
    @GetMapping("{id}")
    public Result<NewsInfoVO> getNews(@PathVariable Long id) {
        return newsService.getNews(id);
    }

}
