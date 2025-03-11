package com.github.bhjj.controller.front;

import com.github.bhjj.constant.ApiRouterConsts;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.ResourceService;
import com.github.bhjj.vo.ImgVerifyCodeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 *  前台门户-资源(图片/视频/文档)模块 API 控制器
 * @author ZhangXianDuo
 * @date 2025/3/11
 */
@Tag(name = "ResourceController", description = "前台门户-资源(图片/视频/文档)接口")
@RequiredArgsConstructor
@RequestMapping(ApiRouterConsts.API_FRONT_RESOURCE_URL_PREFIX)
@RestController
public class ResourceController {
    private final ResourceService resourceService;

    /**
     * 获取图片验证码接口
     */
    @Operation(summary = "获取图片验证码接口")
    @GetMapping("/img_verify_code")
    public Result<ImgVerifyCodeVO> getImgVerifyCode() throws IOException {
        return resourceService.getImgVerifyCode();
    }
}
