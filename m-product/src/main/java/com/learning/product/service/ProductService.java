package com.learning.product.service;

import com.learning.product.entity.Product;
import com.learning.product.repository.ProductRepository;
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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:GR
 * Date:2016/11/9
 * Discription:
 */
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    private static int defaultPageSize = 10;

    public List<Map<String,Object>> loadProduct() throws HzbuviException{
        List<Map<String,Object>> results=new ArrayList<>();
        List<Product> products = productRepository.findAll();
        products.forEach(product -> {
                if ("0".equals(product.getIsDelete())){
                    Map<String,Object> map=new HashMap<>();
                    map.put("prudctId",product.getId());
                    map.put("productName",product.getProductName());
                    map.put("pruductPrice",product.getProductPrice());
                    results.add(map);
                }
        });
        return results;
    }

    public String insert(Product product) {
        if (null == productRepository.findByProductNumber(product.getProductNumber())) {
            product.setIsDelete("0");
            product.setOrgNumber(product.getOrgNumber());
            product.setProductAmount(product.getProductAmount());
            product.setCouponPoolNo(product.getCouponPoolNo());
            product.setProductPrice(product.getProductPrice());
            product.setProductName(product.getProductName());
            product.setProductNumber(product.getProductNumber());
            product.setOrigProductAmount(product.getOrigProductAmount());
            productRepository.save(product);
            return "success";
        }
        return "failure";
    }

    /**
     * 0代表未删除
     * 1代表删除
     *
     * @param productNumber
     * @return
     */
    public String delete(String productNumber) {
        Product product = productRepository.findByProductNumber(productNumber);
        product.setIsDelete("1");
        productRepository.save(product);
        return "success";
    }

    public String update(Product product) {
        Product products = productRepository.findById(product.getId());
        products.setIsDelete("0");
        products.setProductNumber(product.getProductNumber());
        products.setProductName(product.getProductName());
        products.setProductPrice(product.getProductPrice());
        products.setCouponPoolNo(product.getCouponPoolNo());
        products.setOrgNumber(product.getOrgNumber());
        products.setProductAmount(product.getProductAmount());
        products.setOrigProductAmount(product.getOrigProductAmount());
        productRepository.save(products);
        return "success";
    }

    public Map<String, Object> show(Map<String, String> parm) {
        String page = ValueUtil.coalesce(parm.get("page"), "0");
        String productNumber = parm.get("productNumber");
        String productName = parm.get("productName");
        Map<String, String> map1 = new HashMap<>();
        if (ValueUtil.notEmpity(productNumber)) {
            map1.put("productNumber", productNumber);
        }
        if (ValueUtil.notEmpity(productName)) {
            map1.put("productName", productName);
        }
        Pageable pageable = new PageRequest(Integer.valueOf(page), defaultPageSize,new Sort(Sort.Direction.DESC, new String[] { "id" }));
        Page<Product> questionbankPage = productRepository.findAll(getWhereClause(productNumber, productName), pageable);
        Map<String, Object> map = new HashMap<>();
        map.put("page", questionbankPage);
        map.put("condition", map1);
        return map;
    }

    private Specification<Product> getWhereClause(String productNumber, String productName) {
        return new Specification<Product>() {
            @Override
            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (ValueUtil.notEmpity(productNumber)) {
//                    predicate.getExpressions().add(cb.like(root.<Integer>get("productNumber"), Integer.valueOf(productNumber)));
                    predicate.getExpressions().add(cb.like(root.<String>get("productNumber"), "%" + productNumber + "%"));
                }
                if (ValueUtil.notEmpity(productName)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("productName"), "%" + productName + "%"));
                }
                predicate.getExpressions().add(cb.equal(root.<Integer>get("isDelete"), 0));
                return predicate;
            }
        };
    }

    public String findProductPrice(String productNumber) {
        Product product = productRepository.findByProductNumber(productNumber);
        if (ObjectUtil.isEmpty(product)){
            return null;
        }
        return String.valueOf(product.getProductPrice());
    }

    public Product index(Integer id){
        Product product= productRepository.findById(id);
        return product;
    }
}
