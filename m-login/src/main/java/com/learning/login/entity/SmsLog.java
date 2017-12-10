package com.learning.login.entity;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/12/10.
 */
@Entity
@Table(name="smsLog")
public class SmsLog {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    private String phoneNum;
    private String smsContent;
    private String smsDate;
    private String smsResult;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getSmsDate() {
        return smsDate;
    }

    public void setSmsDate(String smsDate) {
        this.smsDate = smsDate;
    }

    public String getSmsResult() {
        return smsResult;
    }

    public void setSmsResult(String smsResult) {
        this.smsResult = smsResult;
    }

    @Override
    public String toString() {
        return "SmsLog{" +
                "id=" + id +
                ", phoneNum='" + phoneNum + '\'' +
                ", smsContent='" + smsContent + '\'' +
                ", smsDate='" + smsDate + '\'' +
                '}';
    }
}
