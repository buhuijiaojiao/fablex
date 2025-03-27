package com.github.bhjj.service.impl;

import com.github.bhjj.dto.BookSearchDTO;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.SearchService;
import com.github.bhjj.vo.BookInfoVO;
import com.github.bhjj.vo.PageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * es搜索 服务实现类
 * @author ZhangXianDuo
 * @date 2025/3/27
 */

@ConditionalOnProperty(prefix = "spring.elasticsearch", name = "enabled", havingValue = "true")
@Service
@RequiredArgsConstructor
@Slf4j
public class EsSearchServiceImpl implements SearchService {
    @Override
    public Result<PageVO<BookInfoVO>> searchBooks(BookSearchDTO condition) {
        //TODO 待集成ES
        return null;
    }
}
