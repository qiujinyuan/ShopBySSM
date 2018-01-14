package com.cdsxt.ro;

import java.util.Date;

/**
 * 订单信息
 */

public class OrderInfo {

    private String oid;
    private Double sumPrice;
    private Date orderTime;
    private String state;
    private String name;
    private String phone;
    private String addr;
    private Integer uid;
    private String logisticsComp;
    private Integer logisticsNum;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Double getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(Double sumPrice) {
        this.sumPrice = sumPrice;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getLogisticsComp() {
        return logisticsComp;
    }

    public void setLogisticsComp(String logisticsComp) {
        this.logisticsComp = logisticsComp;
    }

    public Integer getLogisticsNum() {
        return logisticsNum;
    }

    public void setLogisticsNum(Integer logisticsNum) {
        this.logisticsNum = logisticsNum;
    }
}
