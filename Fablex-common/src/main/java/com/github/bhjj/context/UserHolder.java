package com.github.bhjj.context;

import lombok.experimental.UtilityClass;

/**
 * 上下文
 * @author ZhangXianDuo
 * @date 2025/3/10
 */
@UtilityClass
public class UserHolder {

    /**
     * 当前线程用户ID
     */
    private static final ThreadLocal<Long> userIdTL = new ThreadLocal<>();

    /**
     * 当前线程作家ID
     */
    private static final ThreadLocal<Long> authorIdTL = new ThreadLocal<>();

    public void setUserId(Long userId) {
        userIdTL.set(userId);
    }

    public Long getUserId() {
        return userIdTL.get();
    }

    public void setAuthorId(Long authorId) {
        authorIdTL.set(authorId);
    }

    public Long getAuthorId() {
        return authorIdTL.get();
    }

    public void clear() {
        userIdTL.remove();
        authorIdTL.remove();
    }

}
