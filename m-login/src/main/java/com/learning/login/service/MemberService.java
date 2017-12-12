package com.learning.login.service;

import com.learning.login.entity.Member;
import com.learning.login.repository.MemberRepository;
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
 * Created by Administrator on 2017/12/12.
 */
@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    private static int defaultPageSize = 15;

    /**
     * 查询申请记录
     *
     * @param parm
     * @return
     */
    public Map<String, Object> show(Map<String, String> parm) {
        String page = ValueUtil.coalesce(parm.get("page"), "0");
        String memberId = parm.get("memberId");
        String memberName = parm.get("memberName");
        String memberPhone = parm.get("memberPhone");
        Map<String, String> map1 = new HashMap<>();
        if (ValueUtil.notEmpity(memberId)) {
            map1.put("memberId", memberId);
        }
        if (ValueUtil.notEmpity(memberName)) {
            map1.put("memberName", memberName);
        }
        if (ValueUtil.notEmpity(memberPhone)) {
            map1.put("memberPhone", memberPhone);
        }

        //new Sort(Direction.DESC, new String[] { "id" }
        Pageable pageable = new PageRequest(Integer.valueOf(page), defaultPageSize, new Sort(Sort.Direction.DESC, new String[]{"memberId"}));
        Page<Member> applies = memberRepository.findAll(getWhereClause(memberId, memberName, memberPhone), pageable);

        Map<String, Object> map = new HashMap<>();
        map.put("page", applies);
        map.put("condition", map1);
        return map;
    }

    public Specification<Member> getWhereClause(String memberId, String memberName, String memberPhone) {
        return new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (ValueUtil.notEmpity(memberId)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("memberId"), "%" + memberId + "%"));
                }
                if (ValueUtil.notEmpity(memberName)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("memberName"), "%" + memberName + "%"));
                }
                if (ValueUtil.notEmpity(memberPhone)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("memberPhone"), "%" + memberPhone + "%"));
                }

                query.where(predicate);
                //query.orderBy(cb.desc(root.get("gmtCreate").as(Date.class)));
                query.orderBy(cb.desc(root.<String>get("memberId")));
                return predicate;
            }
        };
    }
}
