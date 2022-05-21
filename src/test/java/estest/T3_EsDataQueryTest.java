package estest;

import cn.hutool.json.JSONUtil;
import com.xxm.Application;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xianXiaoMing
 * @create 2022-05-15 19:17
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class T3_EsDataQueryTest {

    @Autowired
    private RestHighLevelClient client;

    private final RequestOptions options = RequestOptions.DEFAULT;

    private static final String indexName = "test_index";


    /**
     * 数量查询
     */
    @Test
    public void totalCount(){
        // 指定创建时间
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("name", "tom"));

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(queryBuilder);

        CountRequest countRequest = new CountRequest(indexName);
        countRequest.source(sourceBuilder);
        try {
            CountResponse countResponse = client.count(countRequest, options);
            log.info("res:{}", JSONUtil.toJsonStr(countResponse));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 数据查询
     */
    @Test
    public  void  dataQuery(){
        // 查询条件,指定时间并过滤指定字段值
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("name", "tom"));
        queryBuilder.mustNot(QueryBuilders.termQuery("age",5));
        sourceBuilder.query(queryBuilder);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse searchResp = client.search(searchRequest, options);
            List<Map<String,Object>> data = new ArrayList<>() ;
            SearchHit[] searchHitArr = searchResp.getHits().getHits();
            for (SearchHit searchHit:searchHitArr){
                Map<String,Object> temp = searchHit.getSourceAsMap();
                temp.put("id",searchHit.getId()) ;
                data.add(temp);
            }
            log.info("res:{}",JSONUtil.toJsonStr(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 分页查询
     */
    @Test
    public void pageQuery(){
        // 查询条件,指定时间并过滤指定字段值
        //在使用 ElasticSearch 的时候，如果索引中的字段是 text 类型，针对该字段聚合、排序和查询的时候常会出现 Fielddata is disabled on text fields by default. Set fielddata=true 的错误
        Integer offset=0;
        Integer size=20;
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(offset);
        sourceBuilder.size(size);
        sourceBuilder.sort("age", SortOrder.DESC);
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse searchResp = client.search(searchRequest, options);
            List<Map<String,Object>> data = new ArrayList<>() ;
            SearchHit[] searchHitArr = searchResp.getHits().getHits();
            for (SearchHit searchHit:searchHitArr){
                Map<String,Object> temp = searchHit.getSourceAsMap();
                temp.put("id",searchHit.getId()) ;
                data.add(temp);
            }
            log.info("res:{}",data);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




}
