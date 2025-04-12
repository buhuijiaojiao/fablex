package com.github.bhjj.service;

import com.github.bhjj.resp.Result;
import com.github.bhjj.vo.HomeBookVO;
import com.github.bhjj.vo.HomeFriendLinkVO;

import java.util.List;

/**
 * 首页服务
 * @author ZhangXianDuo
 * @date 2025/4/11
 */
public interface HomeService {
    /**
     * 首页推荐
     * @return
     */
    Result<List<HomeBookVO>> listHomeBooks();

    /**
     * 友情链接
     * @return
     */
    Result<List<HomeFriendLinkVO>> listHomeFriendLinks();
}
