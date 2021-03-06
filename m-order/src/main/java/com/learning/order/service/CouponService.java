package com.learning.order.service;

import com.learning.login.entity.Member;
import com.learning.login.repository.MemberRepository;
import com.learning.order.entity.Coupon;
import com.learning.order.repository.CouponRepository;
import com.learning.util.basic.ObjectUtil;
import com.learning.util.basic.ValueUtil;
import com.learning.util.date.DateUtil;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/20.
 */
@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private MemberRepository memberRepository;

    private static int defaultPageSize = 15;

    public Coupon getOneCoupon(String memberPhone){
        Coupon coupon = couponRepository.findFirstByCouponStatusEquals("0");
        if (!ObjectUtil.isEmpty(coupon)&&!ObjectUtil.isEmpty(coupon.getCouponInfo())) {
            coupon.setCouponStatus("1");
            coupon.setGrantMember(memberPhone);
            coupon.setGrantTime(DateUtil.toString(new Date(), "yyyyMMddHHmmss"));
            couponRepository.save(coupon);
        }
        return coupon;
    }

    /**
     * 查询申请记录
     *
     * @param parm
     * @return
     */
    public Map<String, Object> show(Map<String, String> parm) {
        String page = ValueUtil.coalesce(parm.get("page"), "0");
        String couponStatus = parm.get("couponStatus");
        String grantMember = parm.get("grantMember");
        Map<String, String> map1 = new HashMap<>();
        if (ValueUtil.notEmpity(couponStatus)) {
            map1.put("couponStatus", couponStatus);
        }
        if (ValueUtil.notEmpity(grantMember)) {
            map1.put("grantMember", grantMember);
        }

        //new Sort(Direction.DESC, new String[] { "id" }
        Pageable pageable = new PageRequest(Integer.valueOf(page), defaultPageSize, new Sort(Sort.Direction.DESC, new String[]{"couponType"}));
        Page<Coupon> coupons = couponRepository.findAll(getWhereClause(couponStatus, grantMember), pageable);

        Map<String, Object> map = new HashMap<>();
        map.put("page", coupons);
        map.put("condition", map1);
        return map;
    }

    public Specification<Coupon> getWhereClause(String couponStatus, String grantMember) {
        return new Specification<Coupon>() {
            @Override
            public Predicate toPredicate(Root<Coupon> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (ValueUtil.notEmpity(couponStatus)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("couponStatus"), "%" + couponStatus + "%"));
                }
                if (ValueUtil.notEmpity(grantMember)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("grantMember"), "%" + grantMember + "%"));
                }

                query.where(predicate);
                //query.orderBy(cb.desc(root.get("gmtCreate").as(Date.class)));
                query.orderBy(cb.desc(root.<String>get("couponType")));
                return predicate;
            }
        };
    }



    public int PreCoupons(){
        //已开户，未发券的客户
        List<Member> members = memberRepository.findByIsActiveAndIsCouponed("1","0");
        //未发送的礼券
        List<Coupon> coupons = couponRepository.findAll(getWhereClause("0", null));

        int sendcoupons = Math.min(members.size(),coupons.size());
        for(int i = 0; i < sendcoupons;i++){
            Member member = members.get(i);
            Coupon coupon = coupons.get(i);

            member.setIsCouponed("1");
            memberRepository.save(member);
            //待发送
            coupon.setCouponStatus("1");
            coupon.setGrantMember(member.getMemberPhone());
            coupon.setGrantTime(DateUtil.toString(new Date(),"yyyyMMdd"));
            couponRepository.save(coupon);
        }
        //总共待发送礼券数量
        return sendcoupons;
    }

        public   List<Coupon> findSendCoupon(){
            return couponRepository.findAll(getWhereClause("1", null));
        }

        public void save(Coupon coupon){
            couponRepository.save(coupon);
        }

}
