package com.xxm.controller;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xianXiaoMing
 * @create 2022-05-15 17:39
 **/
@RestController
@RequestMapping("es")
public class EsController {

    @Autowired
    private RestHighLevelClient client;


    @GetMapping("creteIndex")
    public Object creteIndex() {
        return null;
    }


}
