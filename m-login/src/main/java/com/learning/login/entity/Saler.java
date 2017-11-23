package com.learning.login.entity;

import javax.persistence.*;
/**
 * Author:GR
 * Date:2016/11/9
 * Discription:销售人员实体类
 */
@Entity
@Table(name="saler")
public class Saler{

    @Id
    private String salerId;//销售人员id
    private String salerPhone;//销售人员手机号码
    private String salerName;//销售人员姓名
    private String netNumber;//网点号

    public String getSalerId() {
        return salerId;
    }

    public void setSalerId(String salerId) {
        this.salerId = salerId;
    }

    public String getSalerPhone() {
        return salerPhone;
    }

    public void setSalerPhone(String salerPhone) {
        this.salerPhone = salerPhone;
    }

    public String getSalerName() {
        return salerName;
    }

    public void setSalerName(String salerName) {
        this.salerName = salerName;
    }

    public String getNetNumber() {
        return netNumber;
    }

    public void setNetNumber(String netNumber) {
        this.netNumber = netNumber;
    }

    @Override
    public String toString() {
        return "Saler{" +
                "salerId='" + salerId + '\'' +
                ", salerPhone='" + salerPhone + '\'' +
                ", salerName='" + salerName + '\'' +
                ", netNumber='" + netNumber + '\'' +
                '}';
    }
}
