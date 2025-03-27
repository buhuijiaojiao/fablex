package com.github.bhjj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.bhjj.dao.BookInfoMapper;
import com.github.bhjj.dto.BookSearchDTO;
import com.github.bhjj.entity.BookInfo;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.SearchService;
import com.github.bhjj.vo.BookInfoVO;
import com.github.bhjj.vo.PageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据库搜索 服务实现类
 *
 * @author ZhangXianDuo
 * @date 2025/3/27
 */
@ConditionalOnProperty(prefix = "spring.elasticsearch", name = "enabled", havingValue = "false")
@Slf4j
@RequiredArgsConstructor
@Service
public class DbSearchServiceImpl implements SearchService {
    private final BookInfoMapper bookInfoMapper;

    @Override
    public Result<PageVO<BookInfoVO>> searchBooks(BookSearchDTO condition) {
        //传入分页参数
        Page<BookInfoVO> page = new Page<>();
        page.setCurrent(condition.getPageNum());
        page.setSize(condition.getPageSize());
        //查询
        List<BookInfo> bookInfos = bookInfoMapper.searchBooks(page, condition);
        return Result.success(PageVO.of(condition.getPageNum(), condition.getPageSize(), page.getTotal(),
                bookInfos.stream().map(bookInfo -> BookInfoVO.builder()
                        .id(bookInfo.getId())
                        .bookName(bookInfo.getBookName())
                        .categoryId(bookInfo.getCategoryId())
                        .categoryName(bookInfo.getCategoryName())
                        .authorId(bookInfo.getAuthorId())
                        .authorName(bookInfo.getAuthorName())
                        .wordCount(bookInfo.getWordCount())
                        .lastChapterName(bookInfo.getLastChapterName())
                        .build()).toList()));

    }
}
