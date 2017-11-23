package com.learning.login.repository;

import com.learning.login.entity.Saler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Author:GR
 * Date:2016/11/9
 * Discription:
 */
public interface SalerRepository extends JpaRepository<Saler,String> {

    Saler findBySalerPhone(String salerPhone);

    @Query("select max (salerId) from Saler")
    String findMaxSalerId();

    Page<Saler> findAll(Specification<Saler> whereClause, Pageable pageable);

}
