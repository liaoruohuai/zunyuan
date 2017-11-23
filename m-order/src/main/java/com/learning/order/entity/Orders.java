package com.learning.order.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Author:GR
 * Date:2016/11/10
 * Discription:订单实体类
 */
@Entity
@Table(name="orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 4363123732503564010L;

    @Id
    private String orderId;//'订单号'
    private String orderType;//'订单类型'
    private String tradeTime;//'交易时间'
    private String tradeStatus;//'交易状态'
    private String saleCode;//'销售码'
    private String origOrderId;//'原订单号'
    private String shopId;//'网店号'
    private String orgNumber;//'所属机构号'
    private String isDelete;//是否删除
    private Integer productId;//商品编号

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getSaleCode() {
        return saleCode;
    }

    public void setSaleCode(String saleCode) {
        this.saleCode = saleCode;
    }

    public String getOrigOrderId() {
        return origOrderId;
    }

    public void setOrigOrderId(String origOrderId) {
        this.origOrderId = origOrderId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getOrgNumber() {
        return orgNumber;
    }

    public void setOrgNumber(String orgNumber) {
        this.orgNumber = orgNumber;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderId='" + orderId + '\'' +
                ", orderType='" + orderType + '\'' +
                ", tradeTime='" + tradeTime + '\'' +
                ", tradeStatus='" + tradeStatus + '\'' +
                ", saleCode='" + saleCode + '\'' +
                ", origOrderId='" + origOrderId + '\'' +
                ", shopId='" + shopId + '\'' +
                ", orgNumber='" + orgNumber + '\'' +
                ", isDelete='" + isDelete + '\'' +
                ", productId=" + productId +
                '}';
    }
}
