package com.learning.order.service;

import com.learning.order.entity.Coupon;
import com.learning.order.repository.CouponRepository;
import com.learning.util.basic.ValueUtil;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/20.
 */
@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;
    private static int defaultPageSize = 15;

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
        Page<Coupon> applies = couponRepository.findAll(getWhereClause(couponStatus, grantMember), pageable);

        Map<String, Object> map = new HashMap<>();
        map.put("page", applies);
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


}
