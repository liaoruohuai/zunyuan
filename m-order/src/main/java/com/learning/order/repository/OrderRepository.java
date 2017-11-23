package com.learning.order.repository;

import com.learning.order.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Author:GR
 * Date:2016/11/10
 * Discription:订单repository
 */
public interface OrderRepository extends JpaRepository<Orders, String> {

    List<Orders> findByTradeTimeGreaterThanEqualAndTradeTimeLessThan(String now, String tomorrow);

    Page<Orders> findAll(Specification<Orders> whereClause, Pageable pageable);

    Orders findByOrderTypeAndSaleCode(String orderType, String saleCode);

    Orders findByOrigOrderIdAndIsDelete(String orgOrderId, String isDelete);

}
