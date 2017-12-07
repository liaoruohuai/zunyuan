package com.learning.order.service;

import com.learning.order.entity.Apply;
import com.learning.order.entity.Orders;
import com.learning.order.repository.ApplyRepository;
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
 * Created by Administrator on 2017/12/6.
 */
@Service
public class ApplyService {
    @Autowired
    private ApplyRepository applyRepository;

    private static int defaultPageSize = 15;

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

    private Specification<Apply> getWhereClause(String mobile, String apply_date, String sales_id,String apply_type) {
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
