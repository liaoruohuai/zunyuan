package com.learning.chepei.controller;

import com.learning.chepei.controller.impl.CreateOrderInfo;
import com.learning.chepei.controller.impl.ReturnOrderInfo;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by zhonghua on 2017/6/15.
 */
@WebService
public interface OrderWebService {

    /**
     * 订单录入
     * @param groupId
     * @param shopId
     * @param salemanId
     * @param goodsId
     * @param orderAmt
     * @param mobile
     * @param custName
     * @return
     */
    @WebMethod
    CreateOrderInfo createOrder(String groupId, String shopId, String salemanId, String goodsId, String orderAmt, String mobile, String custName);


    /**
     * 订单退货
     *
     * @param orgOrderId
     * @return
     */
    @WebMethod
    ReturnOrderInfo returnOrder(String orgOrderId);
}
