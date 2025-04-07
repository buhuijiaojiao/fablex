package com.github.bhjj.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.bhjj.dto.BookSearchDTO;
import com.github.bhjj.entity.BookInfo;
import com.github.bhjj.vo.BookInfoVO;

import java.util.List;

/**
 * 小说信息
 * @author ZhangXianDuo
 * @date 2025/3/27
 */

public interface BookInfoMapper extends BaseMapper<BookInfo> {
    /**
     * 小说搜索
     * @param page mybatis-plus 分页对象
     * @param condition 搜索条件
     * @return 返回结果
     * */
    List<BookInfo> searchBooks(IPage<BookInfoVO> page, BookSearchDTO condition);
}
