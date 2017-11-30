package com.learning.login.service;

import com.learning.login.entity.Saler;
import com.learning.login.entity.User;
import com.learning.login.repository.SalerRepository;
import com.learning.login.repository.UserRepository;
import com.learning.salesNetwork.entity.SalesNetwork;
import com.learning.salesNetwork.repository.SalesNetRepository;
import com.learning.util.basic.ObjectUtil;
import com.learning.util.basic.ValueUtil;
import com.learning.util.exception.HzbuviException;
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

    private static int defaultPageSize = 10;

    public String frontLogin(Saler saler) throws HzbuviException{
        String salerName=saler.getSalerName();
        String salerPhone=saler.getSalerPhone();
        ValueUtil.verify(salerName,"salerName null");
        ValueUtil.verify(salerPhone,"salerPhone null");
        Saler resultSaler = salerRepository.findBySalerPhone(salerPhone);
        if (ObjectUtil.isEmpty(resultSaler)){
            saler.setSalerId(getMaxId(salerRepository.findMaxSalerId()));
            salerRepository.save(saler);
            return saler.getSalerId();
        }
        if (!salerName.equals(resultSaler.getSalerName())){
            resultSaler.setSalerName(saler.getSalerName());
            salerRepository.save(resultSaler);
            return resultSaler.getSalerId();
        }
        return resultSaler.getSalerId();
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
        return map;
    }
    public String insert(Saler saler) {
        if (null == salerRepository.findBySalerPhone(saler.getSalerPhone())) {
            saler.setSalerName(saler.getSalerName());
            saler.setSalerPhone(saler.getSalerPhone());
            saler.setIsInitPwd("0");
            saler.setNetNumber(saler.getNetNumber());
            saler.setSalePwd(saler.getSalerPhone().substring(5));
            saler.setSalerId(saler.getSalerId());

            salerRepository.save(saler);
            return "success";
        }
        return "failure";
    }



}
