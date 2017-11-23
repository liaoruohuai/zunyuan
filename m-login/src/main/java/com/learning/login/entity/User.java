package com.learning.login.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Author:GR
 * Date:2016/11/11
 * Discription:用户表
 */
@Entity
@Table(name="user")
public class User implements Serializable{

    private static final long serialVersionUID = 9121200320406380355L;

    @Id
    private Integer userId;//用户id
    private String loginName;//用户名
    private String password;//密码
    private String isDelete;//是否删除

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                ", isDelete='" + isDelete + '\'' +
                '}';
    }
}
