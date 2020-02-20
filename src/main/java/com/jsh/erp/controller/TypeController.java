package com.jsh.erp.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jsh.erp.datasource.entities.DepotHead;
import com.jsh.erp.datasource.entities.Type;
import com.jsh.erp.service.Type.TypeService;
import com.jsh.erp.service.unit.UnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/type")
public class TypeController {
    private Logger logger = LoggerFactory.getLogger(TypeController.class);
    @Resource
    private TypeService typeService;

    /*
     * 查找1级产品类型表的下拉框
     * @param request
     * @return
     */
    @PostMapping(value = "/selectType")
    public JSONArray selectType(HttpServletRequest request)throws Exception {
        JSONArray arr = new JSONArray();
        try {
            List<Type> typeList = typeService.selectType();
            JSONArray dataArray = new JSONArray();
            if (null != typeList) {
                for (Type type : typeList) {
                    JSONObject item = new JSONObject();
                    item.put("Id", type.getId());
                    item.put("tName", type.getName());
                    dataArray.add(item);
                }
            }

            arr = dataArray;
        } catch(Exception e){
            e.printStackTrace();
        }
        return arr;
    }


    /*
     * 查找2级产品类型表的下拉框
     * @param request
     * @return
     */
    @PostMapping(value = "/selectTypeId")
    public JSONArray selectTypeId(@RequestParam("tId")Long tId, HttpServletRequest request)throws Exception {
        JSONArray arr = new JSONArray();
        try {
            Type type = new Type();
            type.setSales_type(tId);
            List<Type> typeList = typeService.selectTypeId(type);
            JSONArray dataArray = new JSONArray();
            if (null != typeList) {
                for (Type typelist : typeList) {
                    JSONObject item = new JSONObject();
                    item.put("Id", typelist.getId());
                    item.put("Name", typelist.getName());
                    dataArray.add(item);
                }
            }
            arr = dataArray;
        } catch(Exception e){
            e.printStackTrace();
        }
        return arr;
    }



}
