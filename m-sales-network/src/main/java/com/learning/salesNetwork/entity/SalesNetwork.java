package com.learning.salesNetwork.entity;

import com.learning.org.entity.OrgEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Author:GR
 * Date:2016/11/9
 * Discription:销售网点实体类
 */
@Entity
@Table(name = "salesnetwork")
public class SalesNetwork implements Serializable{

    private static final long serialVersionUID = 8022953931654628475L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String netNumber;//网点号
    private String netName;//网店名称
    private String netAddress;//网点地址
    private String orgNumber;//网点所属机构
    private String isDelete;//s删除标志

    @OneToOne
    @JoinColumn(name = "orgNumber", referencedColumnName = "orgNumber", insertable = false, updatable = false)
    private OrgEntity orgEntity;



    public OrgEntity getOrgEntity() {
        return orgEntity;
    }

    public void setOrgEntity(OrgEntity orgEntity) {
        this.orgEntity = orgEntity;
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

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }

    public String getNetNumber() {
        return netNumber;
    }

    public void setNetNumber(String netNumber) {
        this.netNumber = netNumber;
    }

    public String getNetAddress() {
        return netAddress;
    }

    public void setNetAddress(String netAddress) {
        this.netAddress = netAddress;
    }

    public String getOrgNumber() {
        return orgNumber;
    }

    public void setOrgNumber(String orgNumber) {
        this.orgNumber = orgNumber;
    }

    @Override
    public String toString() {
        return "SalesNetwork{" +
                "id=" + id +
                ", netNumber='" + netNumber + '\'' +
                ", netName='" + netName + '\'' +
                ", netAddress='" + netAddress + '\'' +
                ", orgNumber='" + orgNumber + '\'' +
                ", isDelete='" + isDelete + '\'' +
                '}';
    }
}
