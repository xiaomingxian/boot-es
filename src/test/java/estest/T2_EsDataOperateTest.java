package estest;

import com.xxm.Application;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
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
public class T2_EsDataOperateTest {

    @Autowired
    private RestHighLevelClient client;

    private final RequestOptions options = RequestOptions.DEFAULT;

    private final  static    String indexName = "test_index";;

    @Test
    public void insertData() {

        try {
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

    /**
     * 更新数据，可以直接修改索引结构
     */
    @Test
    public void updateData(){

        try {
            HashMap<String, Object> dataMap = new HashMap<>();
            dataMap.put("id","{name=Tom, remark=测试数据, age=18}");
            dataMap.put("filed1","tom-hanks");
            dataMap.put("filed2","tom-hanks-wife");
            dataMap.put("filed3","tom-hanks-beautiful-wife");
            UpdateRequest updateRequest = new UpdateRequest(indexName,"doc", dataMap.remove("id").toString());
            updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            updateRequest.doc(dataMap) ;
            this.client.update(updateRequest, options);
            log.info("success");
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    @Test
    public void delData(){
        try {
            DeleteRequest deleteRequest = new DeleteRequest(indexName,"doc", "1");
            this.client.delete(deleteRequest, options);
        } catch (Exception e){
            e.printStackTrace();
        }


    }

}
