package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.Type;

import java.util.List;

public interface TypeMapper {
    /**
     * 查询1级产品类型
     * @return
     */
    List<Type> selectType();


    /**
     * 查询2级产品类型
     * @return
     */
    List<Type> selectTypeId(Type type);



}
