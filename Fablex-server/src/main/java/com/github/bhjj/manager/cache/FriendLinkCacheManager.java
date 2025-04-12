package com.github.bhjj.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.bhjj.constant.CacheConsts;
import com.github.bhjj.constant.DatabaseConsts;
import com.github.bhjj.dao.HomeFriendLinkMapper;
import com.github.bhjj.entity.HomeFriendLink;
import com.github.bhjj.resp.Result;
import com.github.bhjj.vo.HomeFriendLinkVO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhangXianDuo
 * @date 2025/4/12
 */
@Component
@RequiredArgsConstructor
public class FriendLinkCacheManager {

    public final HomeFriendLinkMapper homeFriendLinkMapper;


    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
            value = CacheConsts.HOME_FRIEND_LINK_CACHE_NAME)
    public Result<List<HomeFriendLinkVO>> listHomeFriendLinks() {
        QueryWrapper<HomeFriendLink> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc(DatabaseConsts.CommonColumnEnum.SORT.getName());
        return Result.success(
                homeFriendLinkMapper.selectList(queryWrapper).stream()
                        .map(v ->
                        {
                            HomeFriendLinkVO homeFriendLinkVO = new HomeFriendLinkVO();
                            homeFriendLinkVO.setLinkName(v.getLinkName());
                            homeFriendLinkVO.setLinkUrl(v.getLinkUrl());
                            return homeFriendLinkVO;
                        }).toList()
        );


    }
}
