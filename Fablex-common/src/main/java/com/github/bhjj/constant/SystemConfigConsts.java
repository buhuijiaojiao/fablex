package com.github.bhjj.constant;

/**
 * @author ZhangXianDuo
 * @date 2025/3/10
 */
public class SystemConfigConsts {

    private SystemConfigConsts() {
        throw new IllegalStateException(CONST_INSTANCE_EXCEPTION_MSG);
    }

    /**
     * Http 请求认证 Header
     */
    public static final String HTTP_AUTH_HEADER_NAME = "Authorization";

    /**
     * 前台门户系统标识
     */
    public static final String FABLEX_FRONT_KEY = "front";

    /**
     * 作家管理系统标识
     */
    public static final String FABLEX_AUTHOR_KEY = "author";

    /**
     * 后台管理系统标识
     */
    public static final String FABLEX_ADMIN_KEY = "admin";

    /**
     * 图片上传目录
     */
    public static final String IMAGE_UPLOAD_DIRECTORY = "/image/";

    /**
     * 常量类实例化异常信息
     */
    public static final String CONST_INSTANCE_EXCEPTION_MSG = "Constant class";

}