package com.learning.order.repository;

import com.learning.order.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */
public interface CouponRepository extends JpaRepository<Coupon,Integer> {

    Page<Coupon> findAll(Specification<Coupon> whereClause, Pageable pageable);

    List<Coupon> findAll(Specification<Coupon> whereClause);

    Coupon findFirstByCouponStatusEquals(String CouponStatus);
}
