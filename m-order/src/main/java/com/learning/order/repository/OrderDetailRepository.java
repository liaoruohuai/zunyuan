package com.learning.order.repository;

import com.learning.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Author:GR
 * Date:2016/11/10
 * Discription:
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer> {

}
