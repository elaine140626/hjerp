package com.jsh.erp.datasource.vo;

import com.jsh.erp.datasource.entities.DepotHead;

public class DepotHeadVo4List extends DepotHead{

    private String projectName;

    private String organName;

    private String handsPersonName;

    private String accountName;

    private String allocationProjectName;

    private String materialsList;

    private String opertimeStr;

    private String supplier;
    private String contacts;
    private String phonenum;
    private String description;
    private Integer supplier_id;

    private Long headerId;

    private String UnitPrice;
    private String Munit;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMunit() {
        return Munit;
    }

    public void setMunit(String munit) {
        Munit = munit;
    }

    public Integer getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(Integer supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        UnitPrice = unitPrice;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getHandsPersonName() {
        return handsPersonName;
    }

    public void setHandsPersonName(String handsPersonName) {
        this.handsPersonName = handsPersonName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAllocationProjectName() {
        return allocationProjectName;
    }

    public void setAllocationProjectName(String allocationProjectName) {
        this.allocationProjectName = allocationProjectName;
    }

    public String getMaterialsList() {
        return materialsList;
    }

    public void setMaterialsList(String materialsList) {
        this.materialsList = materialsList;
    }

    public String getOpertimeStr() {
        return opertimeStr;
    }

    public void setOpertimeStr(String opertimeStr) {
        this.opertimeStr = opertimeStr;
    }
}