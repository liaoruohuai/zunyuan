package com.learning.order.entity;

import javax.persistence.*;

/**
 * Created by Administrator on 2017/12/20.
 */
@Entity
@Table(name="coupon")
public class Coupon {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;
    private String couponType;
    private String couponInfo;
    private String couponValidDate;
    private String couponDesp;
    private String grantMember;
    private String grantTime;
    private String couponStatus; //0-未发送，1-待发送，2-已发送


    public String getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(String couponStatus) {
        this.couponStatus = couponStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(String couponInfo) {
        this.couponInfo = couponInfo;
    }

    public String getCouponValidDate() {
        return couponValidDate;
    }

    public void setCouponValidDate(String couponValidDate) {
        this.couponValidDate = couponValidDate;
    }

    public String getCouponDesp() {
        return couponDesp;
    }

    public void setCouponDesp(String couponDesp) {
        this.couponDesp = couponDesp;
    }

    public String getGrantMember() {
        return grantMember;
    }

    public void setGrantMember(String grantMember) {
        this.grantMember = grantMember;
    }

    public String getGrantTime() {
        return grantTime;
    }

    public void setGrantTime(String grantTime) {
        this.grantTime = grantTime;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", couponType='" + couponType + '\'' +
                ", couponInfo='" + couponInfo + '\'' +
                ", couponValidDate='" + couponValidDate + '\'' +
                ", couponDesp='" + couponDesp + '\'' +
                ", grantMember='" + grantMember + '\'' +
                ", grantTime='" + grantTime + '\'' +
                '}';
    }

}
