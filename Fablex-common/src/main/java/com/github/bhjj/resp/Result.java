package com.github.bhjj.resp;

import com.github.bhjj.enumeration.ErrorCodeEnum;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Http Rest 响应工具及数据格式封装
 *
 * @author ZhangXianDuo
 * @date 2025/3/8
 */
@Getter
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 响应码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    private Result() {
        this.code = ErrorCodeEnum.OK.getCode();
        this.message = ErrorCodeEnum.OK.getMessage();
    }

    private Result(ErrorCodeEnum errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    private Result(T data) {
        this();
        this.data = data;
    }

    /**
     * 业务处理成功,无数据返回
     */
    public static Result<Void> success() {
        return new Result<>();
    }

    /**
     * 业务处理成功，有数据返回
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    /**
     * 业务处理失败
     */
    public static Result<Void> fail(ErrorCodeEnum errorCode) {
        return new Result<>(errorCode);
    }

    /**
     * 系统错误
     */
    public static Result<Void> error() {
        return new Result<>(ErrorCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 判断是否成功 , Json序列化器会自动调用属于@Getter的方法，其中以is开头，后面是大写字母（例如'O'k）的会根据JavaBean的规范，被视为Getter
     */
    public boolean isOk() {
        return Objects.equals(this.code, ErrorCodeEnum.OK.getCode());
    }

}
