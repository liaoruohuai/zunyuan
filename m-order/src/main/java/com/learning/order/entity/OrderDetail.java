package com.learning.order.entity;


import javax.persistence.*;
import java.io.Serializable;

/**
 * Author:GR
 * Date:2016/11/10
 * Discription:订单详情
 */
@Entity
@Table(name="orderdetail")
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1247636721991797720L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;
    private String orderId;//订单id
    private String productName;//商品名称
    private Double productPrice;//商品价格
    private String customerName;//客户名称
    private String customerPhone;//客户手机号
    private String salerName;//销售人员名
    private String netNumber;//网点名称
    private String productNumber;//商品编号
    private String slaerId;//销售人员id

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getSlaerId() {
        return slaerId;
    }

    public void setSlaerId(String slaerId) {
        this.slaerId = slaerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
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
        return "OrderDetail{" +
                "id=" + id +
                ", orderId='" + orderId + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                ", salerName='" + salerName + '\'' +
                ", netNumber='" + netNumber + '\'' +
                ", productNumber='" + productNumber + '\'' +
                ", slaerId='" + slaerId + '\'' +
                '}';
    }


}
