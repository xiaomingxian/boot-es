#### 参数说明

[操作api](https://www.cainiaojc.com/elasticsearch/elasticsearch-populate.html)

```kibana
GET /test_index/_search
{
"from":0,  # 分页
"size":20, # 分页
"version": true # 显示数据版本
}
```

#### 排序
```
GET /test_index/_search
{
  "query":{
    "match_all":{}

  },
   "sort":[{"age":"desc"}]
}
```

#### 修改字段索引类型
```
PUT /test_index/_mapping
{
  "properties": {
    "name": { 
      "type":     "text",
      "fielddata": true
    }
  }
}
```


