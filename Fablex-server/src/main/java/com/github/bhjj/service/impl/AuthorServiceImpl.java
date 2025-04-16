package com.github.bhjj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.bhjj.constant.DatabaseConsts;
import com.github.bhjj.context.UserHolder;
import com.github.bhjj.dao.AuthorInfoMapper;
import com.github.bhjj.dao.BookInfoMapper;
import com.github.bhjj.dto.AuthorRegisterDTO;
import com.github.bhjj.dto.PageBean;
import com.github.bhjj.entity.AuthorInfo;
import com.github.bhjj.entity.BookInfo;
import com.github.bhjj.exception.BusinessException;
import com.github.bhjj.manager.cache.AuthorInfoCacheManager;
import com.github.bhjj.manager.cache.BookInfoCacheManager;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.AuthorService;
import com.github.bhjj.vo.BookInfoVO;
import com.github.bhjj.vo.PageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 作家服务实现类
 *
 * @author ZhangXianDuo
 * @date 2025/3/9
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorInfoMapper authorInfoMapper;

    private final AuthorInfoCacheManager authorInfoCacheManager;

    private final BookInfoMapper bookInfoMapper;

    /**
     * 注册
     * @param authorRegisterDTO
     * @return
     */
    @Override
    public Result<Void> register(AuthorRegisterDTO authorRegisterDTO) {
        // 校验该用户是否已注册为作家
        if(Objects.nonNull(authorInfoCacheManager.getAuthorInfo(authorRegisterDTO.getUserId()))){
            // 该用户已经是作家，直接返回
            return Result.success();
        }
        //保存作家注册信息
        AuthorInfo authorInfo = new AuthorInfo();
        BeanUtils.copyProperties(authorRegisterDTO, authorInfo);
        authorInfo.setInviteCode("0");
        //TODO: AOP
        authorInfo.setCreateTime(LocalDateTime.now());
        authorInfo.setUpdateTime(LocalDateTime.now());
        authorInfoMapper.insert(authorInfo);
        //清除作家缓存
        authorInfoCacheManager.evictAuthorCache();
        return Result.success();
    }

    /**
     * 获取作家状态
     * @param userId
     * @return
     */
    @Override
    public Result<Integer> getStatus(Long userId) {
        //检查缓存
        AuthorInfo authorInfo = authorInfoCacheManager.getAuthorInfo(userId);
        if(Objects.nonNull(authorInfo)){
            return Result.success(authorInfo.getStatus());
        }
        //检查数据库
        QueryWrapper<AuthorInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq(DatabaseConsts.AuthorInfoTable.COLUMN_USER_ID, userId)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        authorInfo = authorInfoMapper.selectOne(queryWrapper);
        if(Objects.isNull(authorInfo)){
            //用户不是作家
            return Result.success(null);
        }
        return Result.success(authorInfo.getStatus());
    }

    /**
     * 名下小说查询
     * @return
     */
    @Override
    public Result<PageVO<BookInfoVO>> listBooks(PageBean dto) {
        //构建分页条件
        IPage<BookInfo> page = Page.of(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq(DatabaseConsts.BookTable.AUTHOR_ID, UserHolder.getAuthorId())
                .orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName());
        //查询
        IPage<BookInfo> bookInfoPage = bookInfoMapper.selectPage(page, queryWrapper);

        return Result.success(
                new PageVO<>(dto.getPageNum(),dto.getPageSize(),bookInfoPage.getTotal(),
                        bookInfoPage.getRecords().stream().map(
                                //只取用需要的属性
                                v->BookInfoVO.builder()
                                        .id(v.getId())
                                        .bookName(v.getBookName())
                                        .picUrl(v.getPicUrl())
                                        .categoryName(v.getCategoryName())
                                        .wordCount(v.getWordCount())
                                        .visitCount(v.getVisitCount())
                                        .updateTime(v.getUpdateTime())
                                        .build()).toList()
                )
        );

    }
}
