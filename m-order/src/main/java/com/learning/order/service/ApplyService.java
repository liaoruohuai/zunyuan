package com.learning.order.service;

import com.learning.login.entity.Member;
import com.learning.login.repository.MemberRepository;
import com.learning.order.entity.Apply;
import com.learning.order.entity.Orders;
import com.learning.order.repository.ApplyRepository;
import com.learning.util.basic.Constants;
import com.learning.util.basic.ObjectUtil;
import com.learning.util.basic.TxnSequence;
import com.learning.util.basic.ValueUtil;
import com.learning.util.comm.RanKit;
import com.learning.util.date.DateUtil;
import com.learning.util.exception.HzbuviException;
import javafx.collections.transformation.SortedList;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;


/**
 * Created by Administrator on 2017/12/6.
 */
@Service
public class ApplyService {
    @Autowired
    private ApplyRepository applyRepository;
    @Autowired
    private MemberRepository memberRepository;

    private static int defaultPageSize = 15;

    /**
     * 查询员工的售卡记录
     */
    public List<Map<String,Object>> getSalerApplyList(String salesId) throws HzbuviException{
        List<Map<String,Object>> results=new ArrayList<>();
        List<Apply> applys = applyRepository.findBySalesIdAndApplyStatus(salesId,"1");

        applys.forEach(apply -> {
            if ("1".equals(apply.getApplyStatus())){
                Map<String,Object> map=new HashMap<>();
                map.put("applyCard",apply.getApplyCard());
                map.put("name",apply.getName());
                map.put("idNum",apply.getIdNum());
                map.put("applyTime",apply.getApplyDate()+apply.getApplyTime());
                map.put("mobile",apply.getMobile());
                map.put("gender",apply.getGender());
                results.add(map);
            }
        });
        return results;
    }

    /**
     * 查询会员自助申卡信息
     *
     */
    public Map<String,Object> findMemberApply(Integer memberId){
        Apply resultApply = applyRepository.findByMemberIdAndApplyTypeIs(memberId,"0");
        Map<String,Object> map=new HashMap<>();
        map.put("IsApply", (ObjectUtil.isEmpty(resultApply))?"NO":"YES");
        if (!ObjectUtil.isEmpty(resultApply)) {
            map.put("applyCard", (ObjectUtil.isEmpty(resultApply.getApplyCard()) == true) ? "NULL" : resultApply.getApplyCard());
            map.put("applyStatus", (ObjectUtil.isEmpty(resultApply.getApplyStatus()) == true) ? "NULL" : resultApply.getApplyStatus());
        }
        return map;
    }

    /**
     * 查询是否已有同卡号申请成功
     *
     */
    public String findOldApply(Apply apply){
        Apply oldApply = applyRepository.findByApplyCardAndApplyResp(apply.getApplyCard(),"0000");
        if (ObjectUtil.isEmpty(oldApply)){
            return "NewCard";
        } else if(oldApply.getName().equals(apply.getName()) &&
                  oldApply.getIdNum().equals(apply.getIdNum()) &&
                  oldApply.getMobile().equals(apply.getMobile())
                ) {
            return "successAlready";
        } else{
            return "ApplyByAnotherOne";
        }
    }

    /**
     * 根据验证码匹配之前提交的申请资料
     *
     */
    public Apply findValidApply(Apply apply){
        Apply oldApply = applyRepository.findByLastUpdateTime(apply.getLastUpdateTime());
        return oldApply;
    }

    /**
     * 会员自助申卡
     *
     */
    public String memberApply(Apply apply){
        Apply resultApply = applyRepository.findByMemberIdAndApplyTypeIs(apply.getMemberId(),"0");
        if (!ObjectUtil.isEmpty(resultApply)){
            return "alreadyApply";
        } else{
            resultApply = new Apply();
            Member member = memberRepository.findByMemberId(apply.getMemberId());
            resultApply.setApplyId(DateUtil.toString(new Date(),"yyyyMMddHHmmss") + TxnSequence.getSellCardID());
            resultApply.setApplyType("0");
            resultApply.setMemberId(member.getMemberId());
            resultApply.setApplyDate(DateUtil.toString(new Date(),"yyyyMMdd"));
            resultApply.setApplyTime(DateUtil.toString(new Date(),"HHmmss"));
            resultApply.setBirth(member.getMemberCertNo().substring(6,14));
            resultApply.setApplyStatus("0");
            resultApply.setGender(member.getMemberGender());
            resultApply.setAddress(apply.getAddress());

            resultApply.setIdNum(member.getMemberCertNo());
            resultApply.setIdDate(member.getMemberCertDate());
            resultApply.setName(member.getMemberName());
            resultApply.setMobile(member.getMemberPhone());
            resultApply.setVocation(member.getMemberVocation());
            resultApply.setSalesId("9999");


            String[] address = apply.getAddress().replace(","," ").split(" ");
            for (int i = 0, len = address.length; i < len; i++) {
                switch (i) {
                    case 0:
                        resultApply.setProvince(address[0]);
                    case 1:
                        resultApply.setCity(address[1]);
                    case 2:
                        resultApply.setCountry(address[2]);
                    default:
                        break;
                }
            }
            applyRepository.save(resultApply);
            return "success";
        }
    }

    /**
     * 员工售卡预设Apply信息
     *
     * @param apply
     * @return
     */
    public Apply salerInitApply(Apply apply){
        apply.setApplyId(DateUtil.toString(new Date(),"yyyyMMddHHmmss") + TxnSequence.getSellCardID());
        apply.setApplyType("1");
        apply.setMemberId(999999);
        apply.setApplyDate(DateUtil.toString(new Date(),"yyyyMMdd"));
        apply.setApplyTime(DateUtil.toString(new Date(),"HHmmss"));
        apply.setBirth(apply.getIdNum().substring(6,14));
        apply.setApplyStatus("0");
        apply.setGender(
                ((Integer.parseInt(apply.getIdNum().substring(16,17)))%2 == 0)?"2":"1"//性别取身份证号第17位，奇数为男，偶数为女
        );
        applyRepository.save(apply);
        return apply;
    }

    /**
     * 更新申请记录
     *
     * @param apply
     * * @param apply
     * @return
     */
    public String updateApplyBySaler(Apply apply){

        Apply resultApply = applyRepository.findByApplyId(apply.getApplyId());

        if (ObjectUtil.isEmpty(resultApply)){
            return "ApplyNotFound";
        }else{
            resultApply.setApplyResp(apply.getApplyResp());
            if (apply.getApplyResp().equals("0000")){
                resultApply.setApplyStatus("1");
            }else {
                resultApply.setApplyStatus("2");
            }
            resultApply.setLastUpdateTime(DateUtil.toString(new Date(),"yyyyMMddHHmmss"));
            applyRepository.save(resultApply);
            return "updateSucc";
        }
    }

    /**
     * 查询申请记录
     *
     * @param parm
     * @return
     */
    public Map<String, Object> show(Map<String, String> parm) {
        String page = ValueUtil.coalesce(parm.get("page"), "0");
        String mobile = parm.get("mobile");
        String applyDate = parm.get("applyDate");
        String salesId = parm.get("salesId");
        String applyType = parm.get("applyType");
        Map<String, String> map1 = new HashMap<>();
        if (ValueUtil.notEmpity(mobile)) {
            map1.put("mobile", mobile);
        }
        if (ValueUtil.notEmpity(applyDate)) {
            map1.put("applyDate", applyDate);
        }
        if (ValueUtil.notEmpity(salesId)) {
            map1.put("salesId", salesId);
        }
        if (ValueUtil.notEmpity(applyType)) {
            map1.put("applyType", applyType);
        }
        //new Sort(Direction.DESC, new String[] { "id" }
        Pageable pageable = new PageRequest(Integer.valueOf(page), defaultPageSize, new Sort(Sort.Direction.DESC, new String[]{"applyDate"}));
        Page<Apply> applies = applyRepository.findAll(getWhereClause(mobile, applyDate, salesId,applyType), pageable);

        Map<String, Object> map = new HashMap<>();
        map.put("page", applies);
        map.put("condition", map1);
        return map;
    }

    public Specification<Apply> getWhereClause(String mobile, String apply_date, String sales_id,String apply_type) {
        return new Specification<Apply>() {
            @Override
            public Predicate toPredicate(Root<Apply> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (ValueUtil.notEmpity(mobile)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("mobile"), "%" + mobile + "%"));
                }
                if (ValueUtil.notEmpity(apply_date)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("applyDate"), "%" + apply_date + "%"));
                }
                if (ValueUtil.notEmpity(apply_type)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("applyType"), "%" + apply_type + "%"));
                }
                if (ValueUtil.notEmpity(sales_id)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("salesId"), "%" + sales_id + "%"));
                }
                query.where(predicate);
                //query.orderBy(cb.desc(root.get("gmtCreate").as(Date.class)));
                query.orderBy(cb.desc(root.<String>get("applyDate")));
                return predicate;
            }
        };
    }
}
