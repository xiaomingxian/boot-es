package estest;

import com.xxm.Application;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * @author xianXiaoMing
 * @create 2022-05-15 18:33
 **/
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@Slf4j
public class EsDataOperateTest {

    @Autowired
    private RestHighLevelClient client;

    private final RequestOptions options = RequestOptions.DEFAULT;

    @Test
    public void insertData() {

        try {
            String indexName = "test_index";
            Map dataMap = new HashMap<String, Object>();
            dataMap.put("name","Tom");
            dataMap.put("age","18");
            dataMap.put("remark","测试数据");
            BulkRequest request = new BulkRequest();
            request.add(new IndexRequest(indexName, "doc").id(UUID.randomUUID().toString())
                    .opType("create").source(dataMap, XContentType.JSON));
            this.client.bulk(request, options);
            log.info("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertBatch(){
        try {
            final List<HashMap<String, Object>> userIndexList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                int finalI = i;
                HashMap<String, Object > myMap  = new HashMap<String, Object>(){{
                    put("id", finalI);
                    put("age", finalI);
                    put("name","tom-"+finalI);
                    put("other",UUID.randomUUID().toString().replace("-",""));
                }};
                userIndexList.add(myMap);

            }

            String indexName = "test_index";
            BulkRequest request = new BulkRequest();
            for (Map<String,Object> dataMap:userIndexList){
                request.add(new IndexRequest(indexName,"doc").id(dataMap.remove("id").toString())
                        .opType("create").source(dataMap,XContentType.JSON));
            }
            this.client.bulk(request, options);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void delData(){
        try {
            String indexName = "test_index";

            DeleteRequest deleteRequest = new DeleteRequest(indexName,"doc", "1");
            this.client.delete(deleteRequest, options);
        } catch (Exception e){
            e.printStackTrace();
        }


    }

}
