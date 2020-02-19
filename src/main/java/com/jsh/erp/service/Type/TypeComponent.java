package com.jsh.erp.service.Type;

import com.jsh.erp.service.ICommonQuery;
import com.jsh.erp.service.unit.UnitResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service(value = "type_component")
@UnitResource
public class TypeComponent implements ICommonQuery {
    @Override
    public Object selectOne(Long id) throws Exception {
        return null;
    }

    @Override
    public List<?> select(Map<String, String> parameterMap) throws Exception {
        return null;
    }

    @Override
    public Long counts(Map<String, String> parameterMap) throws Exception {
        return null;
    }

    @Override
    public int insert(String beanJson, HttpServletRequest request) throws Exception {
        return 0;
    }

    @Override
    public int update(String beanJson, Long id) throws Exception {
        return 0;
    }

    @Override
    public int delete(Long id) throws Exception {
        return 0;
    }

    @Override
    public int batchDelete(String ids) throws Exception {
        return 0;
    }

    @Override
    public int checkIsNameExist(Long id, String name) throws Exception {
        return 0;
    }
}
