package com.github.bhjj.service;

import com.github.bhjj.dto.AuthorRegisterDTO;
import com.github.bhjj.dto.PageBean;
import com.github.bhjj.resp.Result;
import com.github.bhjj.vo.BookInfoVO;
import com.github.bhjj.vo.PageVO;
import jakarta.validation.Valid;

import java.util.List;

/**
 *  作家服务
 * @author ZhangXianDuo
 * @date 2025/4/1
 */
public interface AuthorService {
    /**
     * 注册
     * @param authorRegisterDTO
     * @return
     */
    Result<Void> register(AuthorRegisterDTO authorRegisterDTO);

    /**
     * 获取作家状态
     * @param userId
     * @return
     */
    Result<Integer> getStatus(Long userId);

    /**
     * 名下小说查询
     * @return
     */
    Result<PageVO<BookInfoVO>> listBooks(PageBean dto);
}
