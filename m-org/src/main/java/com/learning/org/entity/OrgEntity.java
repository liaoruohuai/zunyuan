package com.learning.org.entity;

import javax.persistence.*;

/**
 * Created by liqingqing on 2016/11/11.
 */
@Entity
@Table(name="org")
public class OrgEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private  Integer id;
    private String orgNumber; //机构编号
    private String supOrgNumber;//上级机构编号
    private String orgName;//机构名称
    private String isDelete;//是否删除

    public String getOrgNumber() {
        return orgNumber;
    }

    public void setOrgNumber(String orgNumber) {
        this.orgNumber = orgNumber;
    }

    public String getSupOrgNumber() {
        return supOrgNumber;
    }

    public void setSupOrgNumber(String supOrgNumber) {
        this.supOrgNumber = supOrgNumber;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
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

    @Override
    public String toString() {
        return "OrgEntity{" +
                "id=" + id +
                ", orgNumber='" + orgNumber + '\'' +
                ", supOrgNumber='" + supOrgNumber + '\'' +
                ", orgName='" + orgName + '\'' +
                ", isDelete='" + isDelete + '\'' +
                '}';
    }
}
