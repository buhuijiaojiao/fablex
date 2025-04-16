package com.github.bhjj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.bhjj.constant.DatabaseConsts;
import com.github.bhjj.dao.BookChapterMapper;
import com.github.bhjj.dao.BookCommentMapper;
import com.github.bhjj.dao.BookInfoMapper;
import com.github.bhjj.dao.UserInfoMapper;
import com.github.bhjj.dto.PageBean;
import com.github.bhjj.entity.BookChapter;
import com.github.bhjj.entity.BookComment;
import com.github.bhjj.entity.UserInfo;
import com.github.bhjj.manager.cache.*;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.BookService;
import com.github.bhjj.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ZhangXianDuo
 * @date 2025/4/11
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final Integer REC_BOOK_COUNT = 4;

    private final BookCategoryCacheManager bookCategoryCacheManager;

    private final BookRankCacheManager bookRankCacheManager;

    private final BookInfoCacheManager bookInfoCacheManager;

    private final BookChapterCacheManager bookChapterCacheManager;

    private final BookContentCacheManager bookContentCacheManager;

    private final BookChapterMapper bookChapterMapper;

    private final BookInfoMapper bookInfoMapper;

    private final BookCommentMapper bookCommentMapper;
    private final UserInfoMapper userInfoMapper;

    /**
     * 小说列表查询接口
     *
     * @param workDirection
     * @return
     */
    @Override
    public Result<List<BookCategoryVO>> listCategory(Integer workDirection) {
        return Result.success(bookCategoryCacheManager.listCategory(workDirection));
    }

    /**
     * 小说点击榜接口
     *
     * @return
     */
    @Override
    public Result<List<BookRankVO>> listVisitRankBooks() {
        return Result.success(bookRankCacheManager.listVisitRankBooks());
    }

    /**
     * 新书榜接口
     */
    @Override
    public Result<List<BookRankVO>> listNewestRankBooks() {
        return Result.success(bookRankCacheManager.listNewestRankBooks());
    }

    /**
     * 更新榜接口
     *
     * @return
     */
    @Override
    public Result<List<BookRankVO>> listUpdateRankBooks() {
        return Result.success(bookRankCacheManager.listUpdateRankBooks());
    }

    /**
     * 根据id小说查询接口
     *
     * @param bookId
     * @return
     */
    @Override
    public Result<BookInfoVO> getBookById(Long bookId) {
        return Result.success(bookInfoCacheManager.getBookById(bookId));
    }

    /**
     * 根据章节id查询小说信息接口
     *
     * @param chapterId
     * @return
     */
    @Override
    public Result<BookContentAboutVO> getBookContentById(Long chapterId) {
        BookChapterVO bookChapterVO = bookChapterCacheManager.getChapter(chapterId);

        String bookContent = bookContentCacheManager.getBookContentByChapterId(chapterId);

        BookInfoVO bookInfoVO = bookInfoCacheManager.getBookById(bookChapterVO.getBookId());

        return Result.success(
                BookContentAboutVO.builder()
                        .bookInfo(bookInfoVO)
                        .chapterInfo(bookChapterVO)
                        .bookContent(bookContent)
                        .build()
        );
    }


    /**
     * 章节目录查询
     *
     * @param bookId
     * @return
     */
    @Override
    public Result<List<BookChapterVO>> listChapter(Long bookId) {
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
                .orderByAsc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM);
        return Result.success(bookChapterMapper.selectList(queryWrapper).stream().map(
                v -> BookChapterVO.builder()
                        .id(v.getId())
                        .bookId(v.getBookId())
                        .chapterNum(v.getChapterNum())
                        .chapterName(v.getChapterName())
                        .chapterWordCount(v.getWordCount())
                        .chapterUpdateTime(v.getUpdateTime())
                        .isVip(v.getIsVip())
                        .build()
        ).toList());
    }

    /**
     * 获取下一章ID接口
     *
     * @param chapterId
     * @return
     */
    @Override
    public Result<Long> getNextChapterId(Long chapterId) {
        // 查询小说ID 和 章节号
        BookChapterVO chapter = bookChapterCacheManager.getChapter(chapterId);
        Long bookId = chapter.getBookId();
        Integer chapterNum = chapter.getChapterNum();
        // 查询下一章信息并返回章节ID
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
                .gt(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM, chapterNum)
                .orderByAsc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        return Result.success(
                Optional.ofNullable(bookChapterMapper.selectOne(queryWrapper))
                        .map(BookChapter::getId)
                        .orElse(null)
        );
    }

    /**
     * 获取上一章ID接口
     *
     * @param chapterId
     * @return
     */
    @Override
    public Result<Long> getPreChapterId(Long chapterId) {
        // 查询小说ID 和 章节号
        BookChapterVO chapter = bookChapterCacheManager.getChapter(chapterId);
        Long bookId = chapter.getBookId();
        Integer chapterNum = chapter.getChapterNum();
        // 查询上一章信息并返回章节ID
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
                .lt(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM, chapterNum)
                .orderByDesc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        return Result.success(
                Optional.ofNullable(bookChapterMapper.selectOne(queryWrapper))
                        .map(BookChapter::getId)
                        .orElse(null)
        );
    }


    /**
     * 增加小说点击量接口
     */
    @Override
    public Result<Void> addVisitCount(Long bookId) {
//      TODO   待优化:这里直接更新数据库，更新数据库硬抗并发延迟长,点击量时效不能保证（查询数据是对缓存），
//       可以选择直接更新缓存，然后进行数据同步，但采用的是本地缓存，多个实例间缓存不同步，这又是一个问题
        bookInfoMapper.addVisitCount(bookId);
        return Result.success();
    }

    /**
     * 小说最新章节相关信息查询接口
     *
     * @param bookId
     * @return
     */
    @Override
    public Result<BookChapterAboutVO> getLastChapterAbout(Long bookId) {
        //查询小说信息
        BookInfoVO bookInfoVO = bookInfoCacheManager.getBookById(bookId);
        //查询最新章节信息
        BookChapterVO bookChapterVO = bookChapterCacheManager.getChapter(bookInfoVO.getLastChapterId());
        //章节内容
        String content = bookContentCacheManager.getBookContentByChapterId(bookChapterVO.getId());
        // 查询章节总数
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId);
        Long chapterTotal = bookChapterMapper.selectCount(queryWrapper);

        //组装数据，返回
        return Result.success(
                BookChapterAboutVO.builder()
                        .chapterInfo(bookChapterVO)
                        .chapterTotal(chapterTotal)
                        //概要30字
                        .contentSummary(content.substring(0, 30))
                        .build()
        );
    }

    /**
     * 小说推荐列表查询接口
     *
     * @param bookId
     * @return
     */
    @Override
    public Result<List<BookInfoVO>> listRecBooks(Long bookId) throws NoSuchAlgorithmException {
        //查询出当前小说分类id
        Long categoryId = bookInfoCacheManager.getBookById(bookId).getCategoryId();
        //根据id查询同类作品最新作品ID集合
        List<Long> lastUpdateIdList = bookInfoCacheManager.getLastUpdateIdList(categoryId);

        List<BookInfoVO> list = new ArrayList<>();
        List<Integer> recIdIndexList = new ArrayList<>();

        //推荐算法
        int count = 0;
        Random rand = SecureRandom.getInstanceStrong();
        //500以内不放回抽取4次bookId
        while (count < REC_BOOK_COUNT) {
            int recIdIndex = rand.nextInt(lastUpdateIdList.size());
            if (!recIdIndexList.contains(recIdIndex)) {
                recIdIndexList.add(recIdIndex);
                bookId = lastUpdateIdList.get(recIdIndex);
                BookInfoVO bookInfoVO = bookInfoCacheManager.getBookById(bookId);
                list.add(bookInfoVO);
                count++;
            }
        }
        return Result.success(list);

    }

    /**
     * 小说最新评论查询接口
     *
     * @param bookId
     * @return
     */
    @Override
    public Result<BookCommentVO> listNewestComments(Long bookId) {
        //TODO还有待测试，因为评论太少

        //评论总数
        QueryWrapper<BookComment> commentCountQueryWrapper = new QueryWrapper<>();
        commentCountQueryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_BOOK_ID, bookId);
        Long commentTotal = bookCommentMapper.selectCount(commentCountQueryWrapper);

        BookCommentVO bookCommentVO = null;
        if (commentTotal > 0) {
            //查询所有评论信息
            QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .eq(DatabaseConsts.BookCommentTable.COLUMN_BOOK_ID, bookId)
                    .orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName())
                    .last(DatabaseConsts.SqlEnum.LIMIT_5.getSql());


            List<BookComment> bookComments = bookCommentMapper.selectList(queryWrapper);

            //评论的用户信息
            List<Long> userIds = bookComments.stream().map(BookComment::getUserId).toList();
            //索引
            Map<Long, UserInfo> users = userInfoMapper.selectBatchIds(userIds).stream()
                    .collect(Collectors.toMap(UserInfo::getId, Function.identity()));

            //评论列表
            List<BookCommentVO.CommentInfo> comments = bookComments.stream().map(v ->
                    BookCommentVO.CommentInfo.builder()
                            .id(v.getId())
                            .commentContent(v.getCommentContent())
                            //依照用户id从索引中取出需要的用户信息
                            .commentUser(users.get(v.getUserId()).getUsername())
                            .commentUserId(v.getUserId())
                            .commentUserPhoto(users.get(v.getUserId()).getUserPhoto())

                            .commentTime(v.getUpdateTime())
                            .build()
            ).toList();

            bookCommentVO = BookCommentVO.builder()
                    //评论总数
                    .commentTotal(commentTotal)
                    .comments(comments)
                    .build();

        } else {
            bookCommentVO = BookCommentVO.builder()
                    .commentTotal(commentTotal)
                    .comments(null)
                    .build();
        }

        return Result.success(bookCommentVO);

    }

    /**
     * 章节管理分页查询
     * @param bookId
     * @param dto
     * @return
     */
    @Override
    public Result<PageVO<BookChapterVO>> listBookChapters(Long bookId, PageBean dto) {
        //构建查询条件
        IPage<BookChapter> page = Page.of(dto.getPageNum(), dto.getPageSize());
        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
                .orderByDesc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM);
        //查询
        IPage<BookChapter> bookChapterPage = bookChapterMapper.selectPage(page, queryWrapper);
        //组装返回
        return Result.success(
                new PageVO<>(dto.getPageNum(), dto.getPageSize(), bookChapterPage.getTotal(),
                        bookChapterPage.getRecords().stream().map(v->BookChapterVO.builder()
                                .id(v.getId())
                                .bookId(v.getBookId())
                                .chapterNum(v.getChapterNum())
                                .chapterName(v.getChapterName())
                                .chapterWordCount(v.getWordCount())
                                .chapterUpdateTime(v.getUpdateTime())
                                .isVip(v.getIsVip())
                                .build()).toList()
                )
        );
    }

    /**
     * 删除章节接口
     * @param chapterId
     * @return
     */
    @Override
    @Transactional
    public Result<Void> deleteBookChapter(Long chapterId) {
        //TODO待办章节删除接口
        //删除内容表相关内容
        //删除章节信息表记录
        //看是否是最新章节，是的话小说信息表也要更新
        //更新任务推送到mq，待消费（更新到es）



        return null;
    }
}
