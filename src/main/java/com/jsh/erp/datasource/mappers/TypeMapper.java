package com.jsh.erp.datasource.mappers;

import com.jsh.erp.datasource.entities.Type;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TypeMapper {
    /**
     * 查询产品类型
     * @return
     */
    List<Type> selectType();

    List<Type> selectAll(@Param("sales_type") Long sales_type);

}
