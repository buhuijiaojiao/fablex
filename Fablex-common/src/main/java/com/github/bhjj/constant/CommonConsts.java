package com.github.bhjj.constant;

/**
 * 通用常量
 * @author ZhangXianDuo
 * @date 2025/3/8
 */
public class CommonConsts {

    /**
     * 是
     * */
    public static final Integer YES = 1;

    /**
     * 否
     * */
    public static final Integer NO = 0;

    /**
     * 性别常量
     * */
    public enum SexEnum{

        /**
         * 男
         * */
        MALE(0,"男"),

        /**
         * 女
         * */
        FEMALE(1,"女");

        SexEnum(int code,String desc){
            this.code = code;
            this.desc = desc;
        }

        private int code;
        private String desc;

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

    }

    // ...省略若干常量
}
