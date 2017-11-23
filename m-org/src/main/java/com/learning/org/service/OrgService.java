package com.learning.org.service;

import com.learning.org.entity.OrgEntity;
import com.learning.org.repository.OrgRepository;
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
 * Created by liqingqing on 2016/11/11.
 */
@Service
public class OrgService {
    @Autowired
    private OrgRepository orgRepository;
    private static int defaultPageSize = 10;
    public String insert(OrgEntity orgEntity) {
        if (null == orgRepository.findByOrgNumber(orgEntity.getOrgNumber())) {
            orgEntity.setOrgNumber(orgEntity.getOrgNumber());
            orgEntity.setOrgName(orgEntity.getOrgName());
            orgEntity.setSupOrgNumber(orgEntity.getSupOrgNumber());
            orgEntity.setIsDelete("0");
            orgRepository.save(orgEntity);
            return "success";
        }
        return "failure";
    }
    /**
     * 0代表未删除
     * 1代表删除
     *
     * @param orgNumber
     * @return
     */
    public String delete(String orgNumber) {
        OrgEntity orgEntity = orgRepository.findByOrgNumber(orgNumber);
        orgEntity.setIsDelete("1");
        orgRepository.save(orgEntity);
        return "success";
    }

    public String update(OrgEntity orgEntity) {
        OrgEntity orgEntitys = orgRepository.findById(orgEntity.getId());
        orgEntitys.setIsDelete("0");
        orgEntitys.setOrgNumber(orgEntity.getOrgNumber());
        orgEntitys.setOrgName(orgEntity.getOrgName());
        orgEntitys.setSupOrgNumber(orgEntity.getSupOrgNumber());
        orgRepository.save(orgEntitys);
        return "success";
    }
    public Map<String, Object> show(Map<String, String> parm) {
        String page = ValueUtil.coalesce(parm.get("page"), "0");
        String orgNumber = parm.get("orgNumber");
        String orgName = parm.get("orgName");
        Map<String, String> map1 = new HashMap<>();
        if (ValueUtil.notEmpity(orgNumber)) {
            map1.put("orgNumber", orgNumber);
        }
        if (ValueUtil.notEmpity(orgName)) {
            map1.put("orgName", orgName);
        }
        Pageable pageable = new PageRequest(Integer.valueOf(page), defaultPageSize,new Sort(Sort.Direction.DESC, new String[] { "id" }));
        Page<OrgEntity> orderses = orgRepository.findAll(getWhereClause(orgNumber, orgName), pageable);
        Map<String, Object> map = new HashMap<>();
        map.put("page", orderses);
        map.put("condition", map1);
        return map;
    }
    private Specification<OrgEntity> getWhereClause(String orgNumber, String orgName) {
        return new Specification<OrgEntity>() {
            @Override
            public Predicate toPredicate(Root<OrgEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (ValueUtil.notEmpity(orgNumber)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("orgNumber"),  "%" + orgNumber + "%"));
                }
                if (ValueUtil.notEmpity(orgName)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("orgName"), "%" + orgName + "%"));
                }
                predicate.getExpressions().add(cb.equal(root.<String>get("isDelete"), "0"));
                return predicate;
            }
        };
    }

    public OrgEntity index(Integer id){
        OrgEntity orgEntity= orgRepository.findById(id);
        return orgEntity;
    }
}
