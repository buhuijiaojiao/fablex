package com.github.bhjj.service;

import com.github.bhjj.resp.Result;
import com.github.bhjj.vo.NewsInfoVO;

import java.util.List;

/**
 * 新闻服务
 * @author ZhangXianDuo
 * @date 2025/4/12
 */
public interface NewsService {

    /**
     * 最新新闻列表查询接口
     * @return
     */
    Result<List<NewsInfoVO>> listLatestNews();

    /**
     * 根据id查询新闻内容接口
     *
     * @param id
     * @return
     */
    Result<NewsInfoVO> getNews(Long id);
}
