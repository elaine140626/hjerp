package com.jsh.erp.datasource.entities;

public class Type {
    private Long Id ;
    private String name;//产品二级分类Name
    private Long sales_type;//产品一级分类id
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getSales_type() {
        return sales_type;
    }

    public void setSales_type(Long sales_type) {
        this.sales_type = sales_type;
    }
}
