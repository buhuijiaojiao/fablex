package com.github.bhjj.controller.author;

import com.github.bhjj.constant.ApiRouterConsts;
import com.github.bhjj.context.UserHolder;
import com.github.bhjj.dto.AuthorRegisterDTO;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 作家API控制器
 * @author ZhangXianDuo
 * @date 2025/4/6
 */
@RestController
@Tag(name = "AuthorController", description = "作家后台-作者模块")
@Slf4j
@RequestMapping(ApiRouterConsts.API_AUTHOR_URL_PREFIX)
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    /**
     * 作家注册接口
     */
    @Operation(summary = "作家注册接口")
    @PostMapping("register")
    public Result<Void> register(@Valid @RequestBody AuthorRegisterDTO authorRegisterDTO) {
        log.info("作家注册请求参数: {}", authorRegisterDTO);
        authorRegisterDTO.setUserId(UserHolder.getUserId());
        return authorService.register(authorRegisterDTO);
    }
    @Operation(summary = "作家状态接口")
    @GetMapping("status")
    public Result<Integer> getStatus() {
        log.info("作家状态查询");
        return authorService.getStatus(UserHolder.getUserId());
    }

}
