package com.github.bhjj.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 小说数据同步到 elasticsearch 任务
 * @author ZhangXianDuo
 * @date 2025/3/28
 */
@ConditionalOnProperty(prefix = "spring.elasticsearch", name = "enabled", havingValue = "true")
@Component
@RequiredArgsConstructor
@Slf4j
public class BookToEsTask {
    //TODO 待实现, 同步小说数据到 elasticsearch,不然es里只有空的索引
}
