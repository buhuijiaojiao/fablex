package com.github.bhjj.controller.front;

import com.github.bhjj.constant.ApiRouterConsts;
import com.github.bhjj.dto.UserRegisterDTO;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.UserInfoService;
import com.github.bhjj.vo.UserRegisterVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前台门户-会员模块 API 控制器
 *
 * @author ZhangXianDuo
 * @date 2025/3/9
 */
@RestController
@Slf4j
@Tag(name = "UserController", description = "前台门户-会员模块")
@RequestMapping(ApiRouterConsts.API_FRONT_USER_URL_PREFIX)
@RequiredArgsConstructor
public class UserController {

    private final UserInfoService userInfoService;


    @PostMapping("/register")
    @Operation(summary = "用户注册接口")
    public Result<UserRegisterVO> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        return userInfoService.register(userRegisterDTO);
    }
}
