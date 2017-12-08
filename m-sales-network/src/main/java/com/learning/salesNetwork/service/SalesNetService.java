package com.learning.salesNetwork.service;

import com.learning.salesNetwork.entity.SalesNetwork;
import com.learning.salesNetwork.repository.SalesNetRepository;
import com.learning.util.basic.ValueUtil;
import com.learning.util.exception.HzbuviException;
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
 * Author:GR
 * Date:2016/11/9
 * Discription:销售网点service
 */
@Service
public class SalesNetService {
    @Autowired
    private SalesNetRepository salesNetRepository;

    private static int defaultPageSize=10;

    public List<SalesNetwork> loadSalesNetwork() throws HzbuviException{
        return salesNetRepository.findAll();
    }

    public String insert(SalesNetwork salesNetwork) {
        if (null == salesNetRepository.findByNetNumber(salesNetwork.getNetNumber())) {
            salesNetwork.setNetNumber(salesNetwork.getNetNumber());
            salesNetwork.setOrgNumber(salesNetwork.getOrgNumber());
            salesNetwork.setNetAddress(salesNetwork.getNetAddress());
            salesNetwork.setNetName(salesNetwork.getNetName());
            salesNetwork.setIsDelete("0");
            salesNetRepository.save(salesNetwork);
            return "success";
        }
        return "failure";
    }

    /**0代表未删除
     * 1代表删除
     * @param netNumber
     * @return
     */
    public String delete(String netNumber){
        SalesNetwork salesNetwork=salesNetRepository.findByNetNumber(netNumber);
        salesNetwork.setIsDelete("1");
        salesNetRepository.save(salesNetwork);
        return "success";
    }
    public String update(SalesNetwork salesNetwork){
        SalesNetwork salesNetworks=salesNetRepository.findById(salesNetwork.getId());
        salesNetworks.setIsDelete("0");
        salesNetworks.setNetName(salesNetwork.getNetName());
        salesNetworks.setNetAddress(salesNetwork.getNetAddress());
        salesNetworks.setOrgNumber(salesNetwork.getOrgNumber());
        salesNetRepository.save(salesNetworks);
        return "success";
    }
    public Map<String,Object> show(Map<String,String> parm){
        String page = ValueUtil.coalesce( parm.get("page"),"0");
        String netName= parm.get("netName");
        String netNumber= parm.get("netNumber");
        String netAddress = parm.get("netAddress");
        Map<String,String> map1=new HashMap<>();
        if (ValueUtil.notEmpity(netName)) {
            map1.put("netName",netName);
        }
        if (ValueUtil.notEmpity(netNumber)) {
            map1.put("netNumber",netNumber);
        }
        if (ValueUtil.notEmpity(netAddress)) {
            map1.put("netAddress",netAddress);
        }
        Pageable pageable =new PageRequest(Integer.valueOf(page),defaultPageSize,new Sort(Sort.Direction.DESC, new String[] { "id" }));
        Page<SalesNetwork> questionbankPage = salesNetRepository.findAll(getWhereClause(netName,netNumber,netAddress), pageable);
        Map<String,Object> map = new HashMap<>();
        map.put("page",questionbankPage);
        map.put("condition",map1);
        return map;
    }
    private Specification<SalesNetwork> getWhereClause(final String netName, final String netNumber, final String netAddress) {
        return new Specification<SalesNetwork>() {
            @Override
            public Predicate toPredicate(Root<SalesNetwork> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (ValueUtil.notEmpity(netNumber) ) {
                    predicate.getExpressions().add( cb.like(root.<String>get("netNumber"),"%"+netNumber+"%") );
                }
                if (ValueUtil.notEmpity(netName)) {
                    predicate.getExpressions().add( cb.like(root.<String>get("netName"),"%"+netName+"%") );
                }
                if (ValueUtil.notEmpity(netAddress)) {
                    predicate.getExpressions().add( cb.like(root.<String>get("netAddress"),"%"+netAddress+"%") );
                }
                predicate.getExpressions().add( cb.equal(root.<Integer>get("isDelete"),0));
                return predicate;
            }
        };
    }

    public SalesNetwork index(Integer id){
        SalesNetwork salesNetwork= salesNetRepository.findById(id);
        return salesNetwork;
    }
}
