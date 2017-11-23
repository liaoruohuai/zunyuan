package com.learning.product.repository;

import com.learning.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author:GR
 * Date:2016/11/9
 * Discription:
 */
public interface ProductRepository extends JpaRepository<Product,Integer> {
    Product findByProductNumber(String productNumber);
    Page<Product> findAll(Specification<Product> whereClause, Pageable pageable);
    Product findById(Integer id);
}
