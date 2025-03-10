package com.github.bhjj.dao;

import com.github.bhjj.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author ZhangXianDuo
 * @date 2025/3/10
 */
@Mapper
public interface UserInfoMapper {

    /**
     * 根据id查询用户信息
     * @param userId
     * @return
     */
    @Select("select * from user_info where id = #{userId}")
    UserInfo selectById(Long userId);
}
