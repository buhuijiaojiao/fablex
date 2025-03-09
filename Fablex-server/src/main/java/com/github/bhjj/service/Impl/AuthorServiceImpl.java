package com.github.bhjj.service.Impl;

import com.github.bhjj.dao.AuthorMapper;
import com.github.bhjj.dto.AuthorLoginDTO;
import com.github.bhjj.entity.AuthorInfo;
import com.github.bhjj.enumeration.ErrorCodeEnum;
import com.github.bhjj.exception.BusinessException;
import com.github.bhjj.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZhangXianDuo
 * @date 2025/3/9
 */
@Service
@Slf4j
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private AuthorMapper authorMapper;

    /**
     * 作家登录
     *
     * @param authorLoginDTO
     * @return
     */
    @Override
    public void login(AuthorLoginDTO authorLoginDTO) {
        AuthorInfo authorInfo = authorMapper.select(authorLoginDTO);
        if (authorInfo == null) {
            log.info("登录失败");
            throw new BusinessException(ErrorCodeEnum.USER_ERROR);
        }
    }
}
