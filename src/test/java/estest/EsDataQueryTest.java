package estest;

import com.xxm.Application;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xianXiaoMing
 * @create 2022-05-15 19:17
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EsDataQueryTest {

    @Autowired
    private RestHighLevelClient client;

    private final RequestOptions options = RequestOptions.DEFAULT;

    private static final String indexName = "test_index";


    @Test
    public void totalCount(){
        // 指定创建时间
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("createTime", 1611378102795L));

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(queryBuilder);

        CountRequest countRequest = new CountRequest(indexName);
        countRequest.source(sourceBuilder);
        try {
            CountResponse countResponse = client.count(countRequest, options);
            log.info("res:{}",countRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
