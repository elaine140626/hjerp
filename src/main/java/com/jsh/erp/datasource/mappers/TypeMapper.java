package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.Type;

import java.util.List;

public interface TypeMapper {
    /**
     * 查询产品类型
     * @return
     */
    List<Type> selectType();
}
