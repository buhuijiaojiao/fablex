package com.github.bhjj.controller.front;

import com.github.bhjj.constant.ApiRouterConsts;
import com.github.bhjj.dto.UserLoginDTO;
import com.github.bhjj.dto.UserRegisterDTO;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.UserService;
import com.github.bhjj.vo.UserLoginVO;
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

    private final UserService userService;


    @PostMapping("/register")
    @Operation(summary = "用户注册接口")
    public Result<UserRegisterVO> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册接口，参数：{}", userRegisterDTO);
        return userService.register(userRegisterDTO);
    }

    /**
     * 用户登录接口
     */
    @Operation(summary = "用户登录接口")
    @PostMapping("login")
    public Result<UserLoginVO> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录接口，参数：{}", userLoginDTO);
        return userService.login(userLoginDTO);
    }
}
