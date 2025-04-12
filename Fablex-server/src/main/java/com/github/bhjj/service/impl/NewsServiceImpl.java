package com.github.bhjj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.bhjj.constant.DatabaseConsts;
import com.github.bhjj.dao.NewsContentMapper;
import com.github.bhjj.dao.NewsInfoMapper;
import com.github.bhjj.entity.NewsContent;
import com.github.bhjj.entity.NewsInfo;
import com.github.bhjj.manager.cache.NewsCacheManager;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.NewsService;
import com.github.bhjj.vo.NewsInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 新闻服务实现类
 *
 * @author ZhangXianDuo
 * @date 2025/4/12
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsCacheManager newsCacheManager;

    private final NewsInfoMapper newsInfoMapper;

    private final NewsContentMapper newsContentMapper;


    /**
     * 最新新闻列表查询接口
     * @return
     */
    @Override
    public Result<List<NewsInfoVO>> listLatestNews() {
        return Result.success(newsCacheManager.listLatestNews());
    }

    /**
     * 根据id查询新闻内容接口
     *
     * @param id
     * @return
     */
    @Override
    public Result<NewsInfoVO> getNews(Long id) {
        NewsInfo newsInfo = newsInfoMapper.selectById(id);
        QueryWrapper<NewsContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.NewsContentTable.COLUMN_NEWS_ID, id)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        NewsContent newsContent = newsContentMapper.selectOne(queryWrapper);
        return Result.success(NewsInfoVO.builder()
                .title(newsInfo.getTitle())
                .sourceName(newsInfo.getSourceName())
                .updateTime(newsInfo.getUpdateTime())
                .content(newsContent.getContent())
                .build());
    }
}
