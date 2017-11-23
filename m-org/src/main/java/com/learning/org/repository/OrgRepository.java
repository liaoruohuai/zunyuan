package com.learning.org.repository;

import com.learning.org.entity.OrgEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by liqingqing on 2016/11/11.
 */
public interface OrgRepository extends JpaRepository<OrgEntity,Integer>{
    Page<OrgEntity> findAll(Specification<OrgEntity> whereClause, Pageable pageable);
    OrgEntity findById (Integer id);
    OrgEntity findByOrgNumber(String orgNumber);
}
