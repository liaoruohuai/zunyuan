package com.learning.order.repository;

import com.learning.order.entity.Apply;
import com.learning.order.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2017/12/6.
 */
public interface  ApplyRepository extends JpaRepository<Apply,String> {

    Page<Apply> findAll(Specification<Apply> whereClause, Pageable pageable);
    
}
