package com.learning.product.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Author:GR
 * Date:2016/11/9
 * Discription:产品表
 */
@Entity
@Table(name="product")
public class Product implements Serializable{
    private static final long serialVersionUID = 8625627404645700385L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;
    private String productNumber;//商品编号
    private String productName;//商品名称
    private Double productPrice;//商品价格
    private Integer productAmount;//商品数量
    private Integer origProductAmount;//原来商品数量
    private String orgNumber;//机构编码
    private String couponPoolNo;//券池子编号
    private String isDelete;//是否删除

    public Integer getOrigProductAmount() {
        return origProductAmount;
    }

    public void setOrigProductAmount(Integer origProductAmount) {
        this.origProductAmount = origProductAmount;
    }

    public String getCouponPoolNo() {
        return couponPoolNo;
    }

    public void setCouponPoolNo(String couponPoolNo) {
        this.couponPoolNo = couponPoolNo;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
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

    public Integer getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(Integer productAmount) {
        this.productAmount = productAmount;
    }

    public String getOrgNumber() {
        return orgNumber;
    }

    public void setOrgNumber(String orgNumber) {
        this.orgNumber = orgNumber;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productNumber='" + productNumber + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productAmount=" + productAmount +
                ", origProductAmount=" + origProductAmount +
                ", orgNumber='" + orgNumber + '\'' +
                ", couponPoolNo='" + couponPoolNo + '\'' +
                ", isDelete='" + isDelete + '\'' +
                '}';
    }
}
