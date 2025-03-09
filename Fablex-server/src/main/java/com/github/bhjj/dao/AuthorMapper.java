package com.github.bhjj.dao;

import com.github.bhjj.dto.AuthorLoginDTO;
import com.github.bhjj.entity.AuthorInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author ZhangXianDuo
 * @date 2025/3/9
 */
@Mapper
public interface AuthorMapper {
    @Select("select * from author_info where pen_name=#{username} and email=#{password}")
    AuthorInfo select(AuthorLoginDTO authorLoginDTO);
}
