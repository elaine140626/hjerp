package com.jsh.erp.service.Type;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jsh.erp.constants.BusinessConstants;
import com.jsh.erp.constants.ExceptionConstants;
import com.jsh.erp.datasource.entities.*;
import com.jsh.erp.datasource.mappers.MaterialMapperEx;
import com.jsh.erp.datasource.mappers.TypeMapper;
import com.jsh.erp.datasource.mappers.UnitMapper;
import com.jsh.erp.datasource.mappers.UnitMapperEx;
import com.jsh.erp.exception.BusinessRunTimeException;
import com.jsh.erp.exception.JshException;
import com.jsh.erp.service.log.LogService;
import com.jsh.erp.service.user.UserService;
import com.jsh.erp.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class TypeService {
    private Logger logger = LoggerFactory.getLogger(TypeService.class);

    @Resource
    private TypeMapper typeMapper;



    public List<Type> selectType()throws Exception {
        List<Type> result=null;
        try{
            result=typeMapper.selectType();
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }

    public List<Type> selectTypeId(Type type)throws Exception {
        List<Type> result=null;
        try{
            result=typeMapper.selectTypeId(type);
        }catch(Exception e){
            JshException.readFail(logger, e);
        }
        return result;
    }


}
