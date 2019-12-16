package com.jsh.erp.controller;

import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.constants.ExceptionConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/productConfig")
@Configuration
//加载配置文件信息
public class TitleController {
    @Value("${product.name}")
    private String proName;

    @RequestMapping(value = "/getTitle")
    public  Object getTitle() {
        JSONObject result = ExceptionConstants.standardSuccess();
        result.put("title",proName);
        return result;
    }
}
