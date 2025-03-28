package com.github.bhjj.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.json.JsonData;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.bhjj.constant.EsConsts;
import com.github.bhjj.dto.BookSearchDTO;
import com.github.bhjj.es.EsBookInfo;
import com.github.bhjj.resp.Result;
import com.github.bhjj.service.SearchService;
import com.github.bhjj.vo.BookInfoVO;
import com.github.bhjj.vo.PageVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * es搜索 服务实现类
 *
 * @author ZhangXianDuo
 * @date 2025/3/27
 */

@ConditionalOnProperty(prefix = "spring.elasticsearch", name = "enabled", havingValue = "true")
@Service
@RequiredArgsConstructor
@Slf4j
public class EsSearchServiceImpl implements SearchService {
    private final ElasticsearchClient esClient;

    @SneakyThrows
    @Override
    public Result<PageVO<BookInfoVO>> searchBooks(BookSearchDTO condition) {
        //TODO 待测试，因为es中还未同步数据
        //构建查询请求
        SearchRequest searchRequest = SearchRequest.of(s -> {
                    //指定索引
                    SearchRequest.Builder searchBuilder = s.index(EsConsts.BookIndex.INDEX_NAME);
                    // 构建检索条件
                    buildSearchCondition(condition, searchBuilder);
                    //指定分页参数
                    searchBuilder.from((condition.getPageNum() - 1) * condition.getPageSize())
                            .size(condition.getPageSize());
                    return searchBuilder;
                }
        );
        //查询
        SearchResponse<EsBookInfo> response = esClient.search(searchRequest, EsBookInfo.class);
        //解析结果
        //总文档数
        TotalHits total = response.hits().total();
        //返回前端的结果
        List<BookInfoVO> listVO = new ArrayList<>();
        //获取击中的文档列表然后反序列化为 EsBookInfo
        List<Hit<EsBookInfo>> hits = response.hits().hits();
        // 类型推断 var 非常适合 for 循环，JDK 10 引入，JDK 11 改进
        for (var hit : hits) {
            EsBookInfo book = hit.source();
            //断言实例不为空
            assert book != null;
            /*
            * hit.highlight() 的返回值: 通常情况下，hit.highlight() 返回一个 Map<String, List<String>> 类型的数据结构。
                String (Map 的 Key): 代表字段名 (例如, "bookName", "authorName").
                List<String> (Map 的 Value): 代表高亮片段的列表。
            *  对于一个字段，可能会有多个高亮片段，特别是当关键词在同一个字段中多次出现时。
            *  例如，如果书名是 "Effective Java and Advanced Java Programming"，
            *  搜索 "Java"，则 hit.highlight().get("bookName")
            *  可能返回 ["Effective <em>Java</em> and Advanced <em>Java</em> Programming"]
            *  或者 ["Effective <em>Java</em>",  "and Advanced <em>Java</em> Programming"] (具体取决于高亮器的配置)。
            *
            *   这里其实是把标签封装到了 书名和作者名中，便于前端处理。
            * */
            if (!CollectionUtils.isEmpty(hit.highlight().get(EsConsts.BookIndex.FIELD_BOOK_NAME))) {
                //只取第一个高亮片段
                book.setBookName(hit.highlight().get(EsConsts.BookIndex.FIELD_BOOK_NAME).get(0));
            }
            if (!CollectionUtils.isEmpty(hit.highlight().get(EsConsts.BookIndex.FIELD_AUTHOR_NAME))) {
                //只取第一个高亮片段
                book.setAuthorName(hit.highlight().get(EsConsts.BookIndex.FIELD_AUTHOR_NAME).get(0));
            }
            //封装返回前端的结果
            listVO.add(BookInfoVO.builder()
                    .id(book.getId())
                    .bookName(book.getBookName())
                    .categoryId(book.getCategoryId())
                    .categoryName(book.getCategoryName())
                    .authorId(book.getAuthorId())
                    .authorName(book.getAuthorName())
                    .wordCount(book.getWordCount())
                    .lastChapterName(book.getLastChapterName())
                    .build());
        }
        //断言总文档对象不为空
        assert total != null;
        return Result.success(
                PageVO.of(condition.getPageNum(), condition.getPageSize(), total.value(), listVO));
    }

    private void buildSearchCondition(BookSearchDTO condition, SearchRequest.Builder searchBuilder) {
        BoolQuery boolQuery = BoolQuery.of(b -> {

            // 只查有字数的小说
            b.must(RangeQuery.of(m -> m
                    .field(EsConsts.BookIndex.FIELD_WORD_COUNT)
                    .gt(JsonData.of(0))
            )._toQuery());

            if (!StringUtils.isBlank(condition.getKeyword())) {
                // 关键词匹配
                b.must((q -> q.multiMatch(t -> t
                        .fields(EsConsts.BookIndex.FIELD_BOOK_NAME + "^2",
                                EsConsts.BookIndex.FIELD_AUTHOR_NAME + "^1.8",
                                EsConsts.BookIndex.FIELD_BOOK_DESC + "^0.1")
                        .query(condition.getKeyword())
                )
                ));
            }

            // 精确查询
            if (Objects.nonNull(condition.getWorkDirection())) {
                b.must(TermQuery.of(m -> m
                        .field(EsConsts.BookIndex.FIELD_WORK_DIRECTION)
                        .value(condition.getWorkDirection())
                )._toQuery());
            }

            if (Objects.nonNull(condition.getCategoryId())) {
                b.must(TermQuery.of(m -> m
                        .field(EsConsts.BookIndex.FIELD_CATEGORY_ID)
                        .value(condition.getCategoryId())
                )._toQuery());
            }

            // 范围查询
            if (Objects.nonNull(condition.getWordCountMin())) {
                b.must(RangeQuery.of(m -> m
                        .field(EsConsts.BookIndex.FIELD_WORD_COUNT)
                        .gte(JsonData.of(condition.getWordCountMin()))
                )._toQuery());
            }

            if (Objects.nonNull(condition.getWordCountMax())) {
                b.must(RangeQuery.of(m -> m
                        .field(EsConsts.BookIndex.FIELD_WORD_COUNT)
                        .lt(JsonData.of(condition.getWordCountMax()))
                )._toQuery());
            }

            if (Objects.nonNull(condition.getUpdateTimeMin())) {
                b.must(RangeQuery.of(m -> m
                        .field(EsConsts.BookIndex.FIELD_LAST_CHAPTER_UPDATE_TIME)
                        .gte(JsonData.of(condition.getUpdateTimeMin().getTime()))
                )._toQuery());
            }

            return b;

        });

        searchBuilder.query(q -> q.bool(boolQuery));


    }
}
