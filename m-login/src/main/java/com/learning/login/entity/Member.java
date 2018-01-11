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
    private String memberLevel;//会员等级,1-自主注册 2-员工售卡导流
    private String memberPoint;//会员积分,
    private String isInitPwd;//是否初始密码修改标志，0-未修改，1-已修改
    private String lastLoginTime;//最后登录时间,

    private String memberCertDate; //会员证件到期日
    private String memberVocation; //会员职业
    private String memberFamilyAddress;//会员家庭地址
    private String memberProvince;//会员省份
    private String memberCity;//会员城市
    private String memberDistrict;//会员县区
    private String memberGender;// 会员性别
    private String memberBirth;//会员生日
    private String registTime;//会员注册时间


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

    public String getMemberCertDate() {
        return memberCertDate;
    }

    public void setMemberCertDate(String memberCertDate) {
        this.memberCertDate = memberCertDate;
    }

    public String getMemberVocation() {
        return memberVocation;
    }

    public void setMemberVocation(String memberVocation) {
        this.memberVocation = memberVocation;
    }

    public String getMemberFamilyAddress() {
        return memberFamilyAddress;
    }

    public void setMemberFamilyAddress(String memberFamilyAddress) {
        this.memberFamilyAddress = memberFamilyAddress;
    }

    public String getMemberProvince() {
        return memberProvince;
    }

    public void setMemberProvince(String memberProvince) {
        this.memberProvince = memberProvince;
    }

    public String getMemberCity() {
        return memberCity;
    }

    public void setMemberCity(String memberCity) {
        this.memberCity = memberCity;
    }

    public String getMemberDistrict() {
        return memberDistrict;
    }

    public void setMemberDistrict(String memberDistrict) {
        this.memberDistrict = memberDistrict;
    }

    public String getMemberGender() {
        return memberGender;
    }

    public void setMemberGender(String memberGender) {
        this.memberGender = memberGender;
    }

    public String getMemberBirth() {
        return memberBirth;
    }

    public void setMemberBirth(String memberBirth) {
        this.memberBirth = memberBirth;
    }

    public String getRegistTime() {
        return registTime;
    }

    public void setRegistTime(String registTime) {
        this.registTime = registTime;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", memberPhone='" + memberPhone + '\'' +
                ", memberPwd='" + memberPwd + '\'' +
                ", memberCertType='" + memberCertType + '\'' +
                ", memberCertNo='" + memberCertNo + '\'' +
                ", memberLevel='" + memberLevel + '\'' +
                ", memberPoint='" + memberPoint + '\'' +
                ", isInitPwd='" + isInitPwd + '\'' +
                ", lastLoginTime='" + lastLoginTime + '\'' +
                ", memberCertDate='" + memberCertDate + '\'' +
                ", memberVocation='" + memberVocation + '\'' +
                ", memberFamilyAddress='" + memberFamilyAddress + '\'' +
                ", memberProvince='" + memberProvince + '\'' +
                ", memberCity='" + memberCity + '\'' +
                ", memberDistrict='" + memberDistrict + '\'' +
                ", memberGender='" + memberGender + '\'' +
                ", memberBirth='" + memberBirth + '\'' +
                ", registTime='" + registTime + '\'' +
                '}';
    }
}
