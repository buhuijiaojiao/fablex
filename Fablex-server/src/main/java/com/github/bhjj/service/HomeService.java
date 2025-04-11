package com.github.bhjj.service;

import com.github.bhjj.resp.Result;
import com.github.bhjj.vo.HomeBookVO;

import java.util.List;

/**
 * @author ZhangXianDuo
 * @date 2025/4/11
 */
public interface HomeService {
    Result<List<HomeBookVO>> listHomeBooks();
}
