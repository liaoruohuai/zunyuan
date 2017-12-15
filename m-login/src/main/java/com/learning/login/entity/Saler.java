package com.learning.login.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Author:GR
 * Date:2016/11/9
 * Discription:销售人员实体类
 */
@Entity
@Table(name="saler")
public class Saler  implements Serializable {

    @Id
    private String salerId;//销售人员id
    private String salerPhone;//销售人员手机号码
    private String salerName;//销售人员姓名
    private String netNumber;//网点号
    private String salePwd;//密码
    private String isInitPwd;//是否初始密码修改标志，0-未修改，1-已修改

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

    public String getSalePwd() {
        return salePwd;
    }

    public void setSalePwd(String salePwd) {
        this.salePwd = salePwd;
    }

    public String getIsInitPwd() {
        return isInitPwd;
    }

    public void setIsInitPwd(String isInitPwd) {
        this.isInitPwd = isInitPwd;
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
