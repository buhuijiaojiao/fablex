package com.github.bhjj.listener;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.github.bhjj.constant.AmqpConsts;
import com.github.bhjj.constant.EsConsts;
import com.github.bhjj.dao.BookInfoMapper;
import com.github.bhjj.entity.BookInfo;
import com.github.bhjj.es.EsBookInfo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Rabbit 队列监听器
 *
 * @author ZhangXianDuo
 * @date 2025/4/17
 */
@Component
@ConditionalOnProperty(prefix = "spring", name = {"elasticsearch.enabled",
        "amqp.enabled"}, havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class RabbitQueueListener {

    private final BookInfoMapper bookInfoMapper;

    private final ElasticsearchClient esClient;

    /**
     * 监听小说信息改变的 ES 更新队列，更新最新小说信息到 ES
     */
    @RabbitListener(queues = AmqpConsts.BookChangeMq.QUEUE_ES_UPDATE)
    @SneakyThrows
    public void updateEsBook(Long bookId) {
        //查询出要更新的数据
        BookInfo bookInfo = bookInfoMapper.selectById(bookId);
        //构建更新请求 接收响应结果
        IndexResponse response = esClient.index(i -> i
                .index(EsConsts.BookIndex.INDEX_NAME)
                .id(bookInfo.getId().toString())
                .document(EsBookInfo.build(bookInfo))
        );
        log.info("Indexed with version " + response.version());
    }

}
