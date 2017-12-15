package com.learning.login.entity;

import javax.persistence.*;

@Entity
@Table(name="member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberId; //'会员id',
    private String memberName;//会员姓名,
    private String memberPhone ;//会员手机号码,
    private String memberPwd ;//会员密码,
    private String memberCertType;//会员证件类型,
    private String memberCertNo;//会员证件号码,
    private String memberLevel;//会员证件号码,
    private String memberPoint;//会员积分,
    private String isInitPwd;//是否初始密码修改标志，0-未修改，1-已修改
    private String lastLoginTime;//最后登录时间,


    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getMemberPwd() {
        return memberPwd;
    }

    public void setMemberPwd(String memberPwd) {
        this.memberPwd = memberPwd;
    }

    public String getMemberCertType() {
        return memberCertType;
    }

    public void setMemberCertType(String memberCertType) {
        this.memberCertType = memberCertType;
    }

    public String getMemberCertNo() {
        return memberCertNo;
    }

    public void setMemberCertNo(String memberCertNo) {
        this.memberCertNo = memberCertNo;
    }

    public String getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(String memberLevel) {
        this.memberLevel = memberLevel;
    }

    public String getMemberPoint() {
        return memberPoint;
    }

    public void setMemberPoint(String memberPoint) {
        this.memberPoint = memberPoint;
    }

    public String getIsInitPwd() {
        return isInitPwd;
    }

    public void setIsInitPwd(String isInitPwd) {
        this.isInitPwd = isInitPwd;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId='" + memberId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", memberPhone='" + memberPhone + '\'' +
                ", memberPwd='" + memberPwd + '\'' +
                ", memberCertType='" + memberCertType + '\'' +
                ", memberCertNo='" + memberCertNo + '\'' +
                ", memberLevel='" + memberLevel + '\'' +
                ", memberPoint='" + memberPoint + '\'' +
                ", isInitPwd='" + isInitPwd + '\'' +
                ", lastLoginTime='" + lastLoginTime + '\'' +
                '}';
    }
}
