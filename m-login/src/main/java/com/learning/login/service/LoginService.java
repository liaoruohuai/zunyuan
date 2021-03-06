package com.learning.login.service;

import com.learning.login.entity.Member;
import com.learning.login.entity.Saler;
import com.learning.login.entity.SysConfig;
import com.learning.login.entity.User;
import com.learning.login.repository.MemberRepository;
import com.learning.login.repository.SalerRepository;
import com.learning.login.repository.SysConfigRepository;
import com.learning.login.repository.UserRepository;
import com.learning.salesNetwork.entity.SalesNetwork;
import com.learning.salesNetwork.repository.SalesNetRepository;
import com.learning.util.basic.ObjectUtil;
import com.learning.util.basic.ValueUtil;
import com.learning.util.date.DateUtil;
import com.learning.util.encryption.MD5;
import com.learning.util.exception.HzbuviException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:GR
 * Date:2016/11/9
 * Discription:登录service
 */
@Service
public class LoginService {

    @Autowired
    private SalerRepository salerRepository;
    @Autowired
    private SalesNetRepository salesNetRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SysConfigRepository sysConfigRepository;

    private static int defaultPageSize = 10;

    private static final Logger logger = LoggerFactory.getLogger("LoginService");

    public String salerLogin(Saler saler) throws Exception{
        //String salerName=saler.getSalerName();
        String salerPhone=saler.getSalerPhone();
        String salerPwd=saler.getSalePwd();
        System.out.println("This line is for test:" + salerPwd + " " + salerPhone);
        //ValueUtil.verify(salerName,"salerName null");
        //ValueUtil.verify(salerPhone,"salerPhone null");
        Saler resultSaler = salerRepository.findBySalerPhone(salerPhone);

        if (ObjectUtil.isEmpty(resultSaler)){
            return "-2";
        }
        else if (resultSaler.getSalePwd().equals(saler.getSalePwd()))
        {
            return resultSaler.getSalerId();
        }else{
            return "-1";
        }
    }

    public String salerResetPwd(Saler saler) throws HzbuviException{
        String salerPhone=saler.getSalerPhone();
        String salerPwd=saler.getSalePwd();
        System.out.println("This line is for test:" + salerPwd + " " + salerPhone);
        Saler resultSaler = salerRepository.findBySalerPhone(salerPhone);

        if (ObjectUtil.isEmpty(resultSaler))
        {
            return "SalerNotFound";
        }else{
            resultSaler.setSalePwd(salerPwd);
            salerRepository.save(resultSaler);
            return "ResetSucc";
        }
    }

    public String memberLogin(Member member) throws HzbuviException{
        String memberPhone=member.getMemberPhone();
        String memberPwd=member.getMemberPwd();
        System.out.println("This line is for test:" + memberPhone + " " + memberPwd);
        ValueUtil.verify(memberPhone,"memberPhone null");
        Member resultMember = memberRepository.findByMemberPhone(memberPhone);

        if (ObjectUtil.isEmpty(resultMember)){
            return "-2";
        }
        else if (resultMember.getMemberPwd().equals(member.getMemberPwd()))
        {
            resultMember.setLastLoginTime(DateUtil.toString(new Date(),"yyyyMMddHHmmss"));
            if (!ObjectUtil.isEmpty(member.getMemberWxOpenid())&&!"NULL".equals(member.getMemberWxOpenid()))
                resultMember.setMemberWxOpenid(member.getMemberWxOpenid());
            if (!ObjectUtil.isEmpty(member.getMemberHeadImgUrl())&&!"NULL".equals(member.getMemberHeadImgUrl()))
                resultMember.setMemberHeadImgUrl(member.getMemberHeadImgUrl());
            memberRepository.save(resultMember);
            return resultMember.getMemberId().toString();
        }else{
            return "-1";
        }
    }

    public String memberRegister(Member member) throws HzbuviException{
        String memberPhone=member.getMemberPhone();
        String memberPwd=member.getMemberPwd();

        System.out.println("This line is for test:" + memberPhone + " " + memberPwd + " Id:" + member.getMemberId());

        Member resultMember = memberRepository.findByMemberPhone(memberPhone);

        if (ObjectUtil.isEmpty(resultMember))
        {
            resultMember = new Member();
            resultMember.setMemberPhone(memberPhone);
            resultMember.setMemberPwd(memberPwd);
            resultMember.setIsInitPwd("1");
            resultMember.setMemberLevel("1");//暂用会员等级表示会员来源，1-自主注册，2-员工售卡导流
            resultMember.setRegistTime(DateUtil.toString(new Date(),"yyyyMMddHHmmss"));//增加会员注册时间记录
            System.out.println(resultMember.toString());
            memberRepository.save(resultMember);
            return "MemberRegisterSucc";
        }
        else if (resultMember.getMemberId()>0){
            return "RegisterDuplicate";
        }else
        {
            return "MemberRegisterFail";
        }
    }

    public String memberResetPwd(Member member) throws HzbuviException{
        String memberPhone=member.getMemberPhone();
        String memberPwd=member.getMemberPwd();
        System.out.println("This line is for test:" + memberPwd + " " + memberPhone);
        Member resultMember = memberRepository.findByMemberPhone(memberPhone);

        if (ObjectUtil.isEmpty(resultMember.getMemberId()))
        {
            return "MemberNotFound";
        }else{
            resultMember.setMemberPwd(memberPwd);
            memberRepository.save(resultMember);
            return "ResetSucc";
        }
    }

    private String getMaxId(String id){
        if (StringUtils.isEmpty(id)){
            return "1";
        }
        return String.valueOf(Integer.parseInt(id)+1);
    }

    public Map<String, Object> show(Map<String, String> parm) throws HzbuviException{
        String page = ValueUtil.coalesce(parm.get("page"), "0");
        String salerName = parm.get("salerName");
        String salerPhone = parm.get("salerPhone");
        String netNumber = parm.get("netNumber");
        Map<String, String> map1 = new HashMap<>();
        if (ValueUtil.notEmpity(salerName)) {
            map1.put("salerName", salerName);
        }
        if (ValueUtil.notEmpity(salerPhone)) {
            map1.put("salerPhone", salerPhone);
        }
        if (ValueUtil.notEmpity(netNumber)) {
            map1.put("netNumber", netNumber);
        }
        Pageable pageable = new PageRequest(Integer.valueOf(page), defaultPageSize,new Sort(Sort.Direction.DESC, new String[] { "salerId" }));
        Page<Saler> questionbankPage = salerRepository.findAll(getWhereClause(salerName, salerPhone,netNumber), pageable);
        Map<String, Object> map = new HashMap<>();
        map.put("page", questionbankPage);
        map.put("condition", map1);
        return map;
    }

    private Specification<Saler> getWhereClause(final String salerName, final String salerPhone,final String netNumber) {
        return new Specification<Saler>() {
            @Override
            public Predicate toPredicate(Root<Saler> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (ValueUtil.notEmpity(salerName)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("salerName"),"%" + salerName + "%" ));
                }
                if (ValueUtil.notEmpity(salerPhone)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("salerPhone"), "%" + salerPhone + "%"));
                }
                if (ValueUtil.notEmpity(netNumber)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("netNumber"), "%" + netNumber + "%"));
                }
                return predicate;
            }
        };
    }

    public Map<String,String> loadSaler(Integer userId) {
        Map<String,String> result=new HashMap<>();
        Saler saler = salerRepository.getOne(String.valueOf(userId));
        SalesNetwork network = salesNetRepository.findByNetNumber(saler.getNetNumber());
        result.put("salerName",saler.getSalerName());
        result.put("netName",network.getNetName());
        return result;
    }

    public User backLogin(User user) throws HzbuviException{
        return userRepository.findByLoginNameAndPasswordAndIsDelete(user.getLoginName(), user.getPassword(), "0");
    }

    public Map<String,Object> findSaler(String salerId) throws HzbuviException{
        Saler saler = salerRepository.findOne(salerId);
        SalesNetwork network = salesNetRepository.findByNetNumber(saler.getNetNumber());
        Map<String,Object> map=new HashMap<>();
        map.put("salerName",saler.getSalerName());
        map.put("netName",network.getNetName());
        map.put("salerId",saler.getSalerId());
        return map;
    }
    public Map<String,Object> findMember(Integer memberId) throws HzbuviException{
        Member member = memberRepository.findByMemberId(memberId);
        Map<String,Object> map=new HashMap<>();
        map.put("memberId",member.getMemberId());
        map.put("memberName",(ObjectUtil.isEmpty(member.getMemberName()) == true) ? "NULL" : member.getMemberName());
        map.put("memberPhone",member.getMemberPhone());
        map.put("memberCertNo",(ObjectUtil.isEmpty(member.getMemberCertNo()) == true) ? "NULL" : member.getMemberCertNo());
        map.put("memberVocation",(ObjectUtil.isEmpty(member.getMemberVocation()) == true) ? "NULL" : member.getMemberVocation());
        map.put("memberCertDate",(ObjectUtil.isEmpty(member.getMemberCertDate()) == true) ? "NULL" : member.getMemberCertDate());
        map.put("memberFamilyAddress",(ObjectUtil.isEmpty(member.getMemberFamilyAddress()) == true) ? "NULL" : member.getMemberFamilyAddress());
        map.put("memberGender",(ObjectUtil.isEmpty(member.getMemberGender()) == true) ? "NULL" : member.getMemberFamilyAddress().replace("1","男").replace("2","女"));
        //map.put("memberProvince",(ObjectUtil.isEmpty(member.getMemberProvince()) == true) ? "NULL" : member.getMemberProvince());
        //会员城市，以省份是否为空来判断有无信息，返回值也是省+城+区拼接，但前台传回来要分开存
        map.put("memberCity",
                (ObjectUtil.isEmpty(member.getMemberProvince()) == true)
                        ? "NULL" : member.getMemberProvince()+ " " + member.getMemberCity() + " " + member.getMemberDistrict());
        map.put("memberInfoFull",
                (
                        ObjectUtil.isEmpty(member.getMemberName()) ||
                        ObjectUtil.isEmpty(member.getMemberCertNo()) ||
                        ObjectUtil.isEmpty(member.getMemberPhone()) ||
                        ObjectUtil.isEmpty(member.getMemberVocation()) ||
                        ObjectUtil.isEmpty(member.getMemberCertDate()) ||
                        ObjectUtil.isEmpty(member.getMemberFamilyAddress()) ||
                        ObjectUtil.isEmpty(member.getMemberProvince())
                )?"NotFull":"Full"
                );
        map.put("memberHeadImgUrl",(ObjectUtil.isEmpty(member.getMemberHeadImgUrl()) == true) ? "NULL" : member.getMemberHeadImgUrl());
        return map;
    }
    public String insert(Saler saler) {
        if (null == salerRepository.findBySalerPhone(saler.getSalerPhone())) {
            saler.setSalerName(saler.getSalerName());
            saler.setSalerPhone(saler.getSalerPhone());
            saler.setIsInitPwd("0");
            saler.setNetNumber(saler.getNetNumber());
            try {
                saler.setSalePwd(MD5.getMD5(saler.getSalerPhone().substring(5)));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            saler.setSalerId(saler.getSalerId());

            salerRepository.save(saler);
            return "success";
        }
        return "failure";
    }

    public Saler index(String salerId){
        System.out.println("############"+salerId);
        Saler sales= salerRepository.findBySalerId(salerId);
        return sales;
    }
    public String delete(String salerId){

        salerRepository.delete(salerId);
        return "success";
    }

    public String update(Saler saler){
        Saler newsaler=salerRepository.findBySalerId(saler.getSalerId());
        newsaler.setSalerName(saler.getSalerName());
        newsaler.setSalerPhone(saler.getSalerPhone());
        newsaler.setNetNumber(saler.getNetNumber());
        newsaler.setSalePwd(saler.getSalePwd());
        newsaler.setIsInitPwd(saler.getIsInitPwd());
        salerRepository.save(newsaler);
        return "success";
    }
    public String updateByPhone(Saler saler){
        Saler newsaler=salerRepository.findBySalerId(saler.getSalerPhone());
        newsaler.setSalerName(saler.getSalerName());
        newsaler.setSalerPhone(saler.getSalerPhone());
        newsaler.setNetNumber(saler.getNetNumber());
        newsaler.setSalePwd(saler.getSalePwd());
        newsaler.setIsInitPwd(saler.getIsInitPwd());
        salerRepository.save(newsaler);
        return "success";
    }

    public SysConfig getAccessToken(){
        return sysConfigRepository.findByKeyName("access_token");
    }

    public String getAccessTokenValue(){
        return sysConfigRepository.findByKeyName("access_token").getContent();
    }

    public String saveAccessToken(String newToken){
        SysConfig newsys = sysConfigRepository.findByKeyName("access_token");
        newsys.setLastTime(DateUtil.toString(new Date(),"yyyyMMddHHmmss"));
        newsys.setContent(newToken);
        sysConfigRepository.save(newsys);
        logger.info("本次Token更新时间=[" + newsys.getLastTime() + "], Content=[" + newsys.getContent() + "]" );
        return "success";
    }

    public Integer findMemberByWx(Member member){
        try {
            Member newMemebr = memberRepository.findByMemberWxOpenid(member.getMemberWxOpenid());
            if (ObjectUtil.isEmpty(newMemebr)) {
                return -1;
            } else {
                newMemebr.setMemberHeadImgUrl(member.getMemberHeadImgUrl());
                memberRepository.save(newMemebr);
                return newMemebr.getMemberId();
            }
        }catch (Exception e){
            logger.info(ValueUtil.toError("findException", e.getMessage()));
            return -2;
        }

    }
}
