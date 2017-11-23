package com.learning.salesNetwork.repository;

import com.learning.salesNetwork.entity.SalesNetwork;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Author:GR
 * Date:2016/11/9
 * Discription:销售网点repository
 */
public interface SalesNetRepository extends JpaRepository<SalesNetwork,String> {
    @Query("select max(id) from SalesNetwork")
    Integer getMaxId();
    SalesNetwork findByNetNumber(String netNumber);
    Page<SalesNetwork> findAll(Specification<SalesNetwork> whereClause, Pageable pageable);
    SalesNetwork findById(Integer id);

}
