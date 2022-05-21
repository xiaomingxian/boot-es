package estest;

import com.xxm.Application;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xianXiaoMing
 * @create 2022-05-15 17:57
 **/
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@Slf4j
public class T1_EsIndexOperateTest {

    @Autowired
    private RestHighLevelClient client;

    private final RequestOptions options = RequestOptions.DEFAULT;

    @Test
    public void exist() {
        try {
            final boolean exists = client.indices().exists(new GetIndexRequest(".kibana"), options);
            log.info("res:{}", exists);
        } catch (Exception e) {
            log.error("e:", e);
        }
    }

    /**
     * 创建索引
     */
    @Test
    public void createIndex() {
        try {
            final HashMap<String, Object> columnMap = new HashMap<>();
            //todo 为什么必须是小写
            CreateIndexRequest request = new CreateIndexRequest("test_index");
            if (columnMap != null && columnMap.size() > 0) {
                Map<String, Object> source = new HashMap<>();
                source.put("properties", columnMap);
                request.mapping(source);
            }
            this.client.indices().create(request, options);
        } catch (Exception e) {
            log.error("e:", e);
        }
    }

    /**
     * 删除索引 todo 不成功
     */
    @Test
    public void delIndex() {
        try {

            String indexName="test_index";
            DeleteRequest request=new DeleteRequest(indexName);
            DeleteResponse response= client.delete(request,RequestOptions.DEFAULT);
            log.info("res:{}", response);
        }catch (Exception e){
            log.error("e:",e);
        }

    }

}
