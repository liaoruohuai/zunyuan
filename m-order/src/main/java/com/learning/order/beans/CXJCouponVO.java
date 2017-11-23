package com.learning.order.beans;

/**
 * Author:GR
 * Date:2016/11/14
 * Discription:
 */
public class CXJCouponVO {

    private String channel;
    private String couponPoolNo;
    private String salesAmt;
    private String receiveAmt;
    private String mobile;
    private String custName;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCouponPoolNo() {
        return couponPoolNo;
    }

    public void setCouponPoolNo(String couponPoolNo) {
        this.couponPoolNo = couponPoolNo;
    }

    public String getSalesAmt() {
        return salesAmt;
    }

    public void setSalesAmt(String salesAmt) {
        this.salesAmt = salesAmt;
    }

    public String getReceiveAmt() {
        return receiveAmt;
    }

    public void setReceiveAmt(String receiveAmt) {
        this.receiveAmt = receiveAmt;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    @Override
    public String toString() {
        return "CXJCouponVO{" +
                "channel='" + channel + '\'' +
                ", couponPoolNo='" + couponPoolNo + '\'' +
                ", salesAmt='" + salesAmt + '\'' +
                ", receiveAmt='" + receiveAmt + '\'' +
                ", mobile='" + mobile + '\'' +
                ", custName='" + custName + '\'' +
                '}';
    }
}
