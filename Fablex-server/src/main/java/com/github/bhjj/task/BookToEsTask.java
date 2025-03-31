package com.github.bhjj.task;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.bhjj.constant.DatabaseConsts;
import com.github.bhjj.constant.EsConsts;
import com.github.bhjj.dao.BookInfoMapper;
import com.github.bhjj.entity.BookInfo;
import com.github.bhjj.es.EsBookInfo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 小说数据同步到 elasticsearch 任务
 *
 * @author ZhangXianDuo
 * @date 2025/3/28
 */
@ConditionalOnProperty(prefix = "spring.elasticsearch", name = "enabled", havingValue = "true")
@Component
@RequiredArgsConstructor
@Slf4j
public class BookToEsTask {

    private final BookInfoMapper bookInfoMapper;

    private final ElasticsearchClient elasticsearchClient;
    /**
     * 每月凌晨做一次全量数据同步
     */
    @SneakyThrows
    @XxlJob("saveToEsJobHandler")
    public ReturnT<String> saveToEs() {
        try {
            //分页查询数据库 每页30条
            QueryWrapper<BookInfo> queryWrapper = new QueryWrapper<>();
            List<BookInfo> bookInfos;
            long maxId = 0;
            for (; ; ) {
                //清理条件
                queryWrapper.clear();
                //从最大id查询30条数据
                queryWrapper
                        .orderByAsc(DatabaseConsts.CommonColumnEnum.ID.getName())
                        .gt(DatabaseConsts.CommonColumnEnum.ID.getName(), maxId)
                        .gt(DatabaseConsts.BookTable.COLUMN_WORD_COUNT, 0)
                        .last(DatabaseConsts.SqlEnum.LIMIT_30.getSql());
                bookInfos = bookInfoMapper.selectList(queryWrapper);
                if (bookInfos.isEmpty()) {
                    break;
                }

                //构建es请求
                BulkRequest.Builder br = new BulkRequest.Builder();
                for (BookInfo book : bookInfos) {
                    //遍历，加入要插入的记录
                    br.operations(op -> op
                            .index(idx -> idx
                                    .index(EsConsts.BookIndex.INDEX_NAME)
                                    .id(book.getId().toString())
                                    .document(EsBookInfo.build(book))
                            )
                    ).timeout(Time.of(t -> t.time("10s")));
                    //更新最大id
                    maxId = book.getId();
                }
                //发送请求
                BulkResponse result = elasticsearchClient.bulk(br.build());

                // Log errors, if any
                if (result.errors()) {
                    log.error("Bulk had errors");
                    for (BulkResponseItem item : result.items()) {
                        if (item.error() != null) {
                            log.error(item.error().reason());
                        }
                    }
                }
            }
            //返回定时任务的结果
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ReturnT.FAIL;
        }
    }
}
