package com.github.bhjj.controller.author;

import com.github.bhjj.constant.ApiRouterConsts;
import com.github.bhjj.dto.AuthorLoginDTO;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhangXianDuo
 * @date 2025/3/9
 */
@RestController
@Slf4j
@RequestMapping(ApiRouterConsts.API_AUTHOR_URL_PREFIX)
public class LoginController {
    @Autowired
    private AuthorService authorService;
    // TODO JWT
    /**
     * 作家登录
     *
     * @param authorLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result<Void> login(@RequestBody AuthorLoginDTO authorLoginDTO) {
        log.info("作家登录: {}", authorLoginDTO.getUsername());
        authorService.login(authorLoginDTO);
        return Result.success();
    }
}
