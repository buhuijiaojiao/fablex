package com.github.bhjj.service.impl;

import com.github.bhjj.manager.cache.HomeBookInfoCacheManager;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.HomeService;
import com.github.bhjj.vo.HomeBookVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ZhangXianDuo
 * @date 2025/4/11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final HomeBookInfoCacheManager homeBookInfoCacheManager;

    @Override
    public Result<List<HomeBookVO>> listHomeBooks() {
        return Result.success(homeBookInfoCacheManager.listHomeBooks());
    }
}
