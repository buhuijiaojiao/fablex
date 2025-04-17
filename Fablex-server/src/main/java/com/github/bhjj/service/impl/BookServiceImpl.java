package com.github.bhjj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.bhjj.constant.DatabaseConsts;
import com.github.bhjj.context.UserHolder;
import com.github.bhjj.dao.*;
import com.github.bhjj.dto.ChapterAddDTO;
import com.github.bhjj.dto.PageBean;
import com.github.bhjj.entity.*;
import com.github.bhjj.enumeration.ErrorCodeEnum;
import com.github.bhjj.manager.cache.*;
import com.github.bhjj.manager.mq.AmqpMsgManager;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.BookService;
import com.github.bhjj.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
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

    private final BookContentMapper bookContentMapper;

    private final AmqpMsgManager amqpMsgManager;

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
     *
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
                        bookChapterPage.getRecords().stream().map(v -> BookChapterVO.builder()
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
     *
     * @param chapterId
     * @return
     */
    @Override
    @Transactional
    public Result<Void> deleteBookChapter(Long chapterId) {
        // 1.查询章节信息
        BookChapterVO chapter = bookChapterCacheManager.getChapter(chapterId);
        // 2.查询小说信息
        BookInfoVO bookInfo = bookInfoCacheManager.getBookById(chapter.getBookId());

        //3.删除章节信息表记录
        bookChapterMapper.deleteById(chapterId);
        //4.删除内容表相关内容
        QueryWrapper<BookContent> deleteBookContentWrapper = new QueryWrapper<>();
        deleteBookContentWrapper.eq(DatabaseConsts.BookContentTable.COLUMN_CHAPTER_ID, chapterId);
        bookContentMapper.delete(deleteBookContentWrapper);
        // 5.更新小说信息
        BookInfo newBookInfo = new BookInfo();
        newBookInfo.setId(bookInfo.getId());
        newBookInfo.setUpdateTime(LocalDateTime.now());
        newBookInfo.setWordCount(bookInfo.getWordCount() - chapter.getChapterWordCount());
        //看是否是最新章节
        if (Objects.equals(bookInfo.getLastChapterId(), chapterId)) {
            //查询出删除后的最新章节
            //这样SQL比较快
            QueryWrapper<BookChapter> bookChapterQueryWrapper = new QueryWrapper<>();
            bookChapterQueryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, chapter.getBookId())
                    .orderByDesc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                    .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
            BookChapter lastBookChapter = bookChapterMapper.selectOne(bookChapterQueryWrapper);
            Long lastChapterId = 0L;
            String lastChapterName = "";
            LocalDateTime lastChapterUpdateTime = null;
            //考虑删除的是第一章的情况
            if (Objects.nonNull(lastBookChapter)) {
                lastChapterId = lastBookChapter.getId();
                lastChapterName = lastBookChapter.getChapterName();
                lastChapterUpdateTime = lastBookChapter.getUpdateTime();
            }
            //提取需要的字段
            newBookInfo.setLastChapterId(lastBookChapter.getId());
            newBookInfo.setLastChapterName(lastBookChapter.getChapterName());
            newBookInfo.setLastChapterUpdateTime(lastBookChapter.getUpdateTime());
        }
        //更新改变的字段
        bookInfoMapper.updateById(newBookInfo);
        //清除缓存
        // 6.清理章节信息缓存
        bookChapterCacheManager.evictBookChapterCache(chapterId);
        // 7.清理章节内容缓存
        bookContentCacheManager.evictBookContentCache(chapterId);
        // 8.清理小说信息缓存
        bookInfoCacheManager.evictBookInfoCache(chapter.getBookId());

        //把更新的id推送到mq
        amqpMsgManager.sendBookChangeMsg(chapter.getBookId());

        return Result.success();
    }

    /**
     * 新建章节接口
     * @return
     */
    @Override
    @Transactional
    public Result<Void> addBookChapter(ChapterAddDTO chapterAddDTO) {
        // 校验该作品是否属于当前作家
        BookInfo bookInfo = bookInfoMapper.selectById(chapterAddDTO.getBookId());
        if (!Objects.equals(bookInfo.getAuthorId(), UserHolder.getAuthorId())) {
            return Result.fail(ErrorCodeEnum.USER_UN_AUTH);
        }
        log.info("校验完成");

        //查询小说总字数
        BookInfo oldBookInfo = bookInfoMapper.selectById(chapterAddDTO.getBookId());
        Integer oldWordCount = oldBookInfo.getWordCount();
        //查询上一章的章节号
        Long lastChapterId = oldBookInfo.getLastChapterId();
        Integer lastChapterNum = bookChapterMapper.selectById(lastChapterId).getChapterNum();
        int chapterNum = 0;
        if(Objects.nonNull(lastChapterNum)) {
            //要考虑是不是第一章
            chapterNum = lastChapterNum + 1;
        }
        log.info("旧的小说信息:{}",oldBookInfo);

        //插入新章节信息
        BookChapter newBookChapter = new BookChapter();
        newBookChapter.setBookId(chapterAddDTO.getBookId());
        newBookChapter.setChapterNum(chapterNum);
        newBookChapter.setChapterName(chapterAddDTO.getChapterName());
        newBookChapter.setIsVip(chapterAddDTO.getIsVip());
        newBookChapter.setWordCount(chapterAddDTO.getChapterContent().length());
        newBookChapter.setUpdateTime(LocalDateTime.now());
        newBookChapter.setCreateTime(LocalDateTime.now());
        //自动返回主键id
        bookChapterMapper.insert(newBookChapter);

        log.info("插入成功新章节信息:{}",newBookChapter);


        //插入新章节内容
        BookContent bookContent = new BookContent();
        bookContent.setChapterId(newBookChapter.getId());
        bookContent.setContent(chapterAddDTO.getChapterContent());
        bookContent.setUpdateTime(LocalDateTime.now());
        bookContent.setCreateTime(LocalDateTime.now());
        bookContentMapper.insert(bookContent);

        log.info("插入成功新章节内容:{}",bookContent);

        //修改小说信息
        BookInfo newBookInfo = new BookInfo();
        newBookInfo.setId(chapterAddDTO.getBookId());
        newBookInfo.setLastChapterId(newBookChapter.getId());
        newBookInfo.setLastChapterName(chapterAddDTO.getChapterName());
        newBookInfo.setWordCount(oldWordCount + chapterAddDTO.getChapterContent().length());
        newBookInfo.setLastChapterUpdateTime(LocalDateTime.now());
        bookInfoMapper.updateById(newBookInfo);

        //删除相关缓存保证一致性
        bookInfoCacheManager.evictBookInfoCache(chapterAddDTO.getBookId());

        //推送要更新的小说id到mq
        amqpMsgManager.sendBookChangeMsg(chapterAddDTO.getBookId());
        return Result.success();
    }
}
