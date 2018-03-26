package com.learning.login.service;

import com.learning.login.entity.Member;
import com.learning.login.repository.MemberRepository;
import com.learning.util.basic.ObjectUtil;
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
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/12.
 */
@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    private static int defaultPageSize = 15;

    public String findOldMember(Member member){
        Member oldMember = memberRepository.findByMemberPhone(member.getMemberPhone());
        if (!ObjectUtil.isEmpty(oldMember)&&!ObjectUtil.isEmpty(oldMember.getMemberPhone())) {
            return "MemberExisted";
        }else{
            return "NewMember";
        }
    }

    public String insert(Member member){
        memberRepository.save(member);
        return member.getMemberId().toString();
    }

    public String updateMemberInfo(Member oldMember){
        Member resultMember = memberRepository.findByMemberId(oldMember.getMemberId());
        if (ObjectUtil.isEmpty(resultMember)){
            return "MemberNotFound";
        } else{
            try {
                resultMember.setMemberName(oldMember.getMemberName());
                resultMember.setMemberCertNo(oldMember.getMemberCertNo());
                resultMember.setMemberGender(oldMember.getMemberGender().replace("男", "1").replace("女", "2"));
                resultMember.setMemberCertType("1");
                resultMember.setMemberVocation(oldMember.getMemberVocation());
                resultMember.setMemberCertDate(oldMember.getMemberCertDate());
                resultMember.setMemberFamilyAddress(oldMember.getMemberFamilyAddress());
                resultMember.setMemberBirth(oldMember.getMemberCertNo().substring(6,14));

                String[] address = oldMember.getMemberCity().split(" ");
                for (int i = 0, len = address.length; i < len; i++) {
                    switch (i) {
                        case 0:
                            resultMember.setMemberProvince(address[0]);
                        case 1:
                            resultMember.setMemberCity(address[1]);
                        case 2:
                            resultMember.setMemberDistrict(address[2]);
                        default:
                            break;
                    }
                }
                memberRepository.save(resultMember);
                return "success";
            }catch (Exception e) {
                return ValueUtil.toError(e.toString(),e.getMessage());
            }
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

    public List<Member> findByIsActiveAndIsCouponed(String isActive, String isCouponed){
        return  memberRepository.findByIsActiveAndIsCouponed(isActive,isCouponed);
    }
}
