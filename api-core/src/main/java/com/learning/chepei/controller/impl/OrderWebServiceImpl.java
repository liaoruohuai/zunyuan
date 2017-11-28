package com.learning.chepei.controller.impl;

import com.learning.chepei.controller.OrderWebService;
import com.learning.order.entity.OrderDetail;
import com.learning.order.entity.Orders;
import com.learning.order.repository.OrderDetailRepository;
import com.learning.order.repository.OrderRepository;
import com.learning.order.service.OrderService;
import com.learning.product.entity.Product;
import com.learning.product.repository.ProductRepository;
import com.learning.util.SMS.SmsUtil;
import com.learning.util.basic.Constants;
import com.learning.util.basic.ObjectUtil;
import com.learning.util.date.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;
import java.util.Date;

/**
 * Created by zhonghua on 2017/6/15.
 */
@WebService(targetNamespace = "http://10.252.15.96:8080/", endpointInterface = "com.learning.chepei.controller.OrderWebService")
public class OrderWebServiceImpl implements OrderWebService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;

    private static final Logger logger = LoggerFactory.getLogger("OrderWebServiceImpl");

    @Transactional
    @Override
    public CreateOrderInfo createOrder(String groupId, String shopId, String salemanId, String goodsId, String orderAmt, String mobile, String custName) {
        CreateOrderInfo createOrderInfo = null;

        // 1.数据校验
        createOrderInfo = this.createOrderValidation(groupId, shopId, salemanId, goodsId, orderAmt, mobile, custName);
        logger.info("----------数据校验----------");

        if (!ObjectUtil.isEmpty(createOrderInfo)) {
            return createOrderInfo;
        }

        // 2.查询产品信息
        Product product = productRepository.findByProductNumber(goodsId);
        logger.info("----------查询产品信息----------");

        if (ObjectUtil.isEmpty(product) || product.getProductAmount() == 0) {
            createOrderInfo = new CreateOrderInfo();

            createOrderInfo.setReturnMsg("产品已经卖完，请联系管理员！");
            createOrderInfo.setReturnCode("error");

            return createOrderInfo;
        }

        // 3.获取销售代码
        String saleCode = orderService.getSellCode(orderAmt, custName, mobile, product.getCouponPoolNo());
        logger.info("----------获取销售代码" + saleCode + "----------");

        // 4.售卖失败
        if (StringUtils.isEmpty(saleCode)) {
            return sellingfailure(goodsId);
        }

        // 5.数据装箱 && 数据入库
        createOrderInfo = this.orderSave(groupId, shopId, salemanId, goodsId, orderAmt, mobile, custName, saleCode);

        // 6.信息反馈
        return createOrderInfo;
    }

    private CreateOrderInfo sellingfailure(String goodsId) {
        Orders order = new Orders();
        orderService.setOrderParam(order);
        order.setOrderType(Constants.ORDER_TYPE_SELL);
        order.setProductId(Integer.parseInt(goodsId));
        order.setTradeStatus(Constants.ORDER_STATUS_FAIL);

        orderRepository.save(order);

        CreateOrderInfo createOrderInfo = new CreateOrderInfo();

        createOrderInfo.setReturnCode("error");
        createOrderInfo.setReturnMsg("售卖失败！");

        return createOrderInfo;
    }

    @Transactional
    @Override
    public ReturnOrderInfo returnOrder(String orgOrderId) {
        ReturnOrderInfo returnOrderInfo = null;

        // 1.数据校验
        returnOrderInfo = this.returnOrderValidation(orgOrderId);

        if (!ObjectUtil.isEmpty(returnOrderInfo)) {
            return returnOrderInfo;
        }

        // 2.数据装箱 && 数据入库
        returnOrderInfo = this.returnOrderAction(orgOrderId);

        // 3.信息反馈
        return returnOrderInfo;
    }

    private ReturnOrderInfo returnOrderAction(String orgOrderId) {
        ReturnOrderInfo returnOrderInfo = new ReturnOrderInfo();
        try {
            // 生产新订单号
            String orderId = orderService.generateOrderId();

            // 获取订单信息
            Orders orders = orderRepository.findByOrigOrderIdAndIsDelete(orgOrderId, "0");

            // 获取销售代码
            String saleCode = orders.getSaleCode();

            // 销售代码不存在
            if (ObjectUtil.isEmpty(saleCode)) {
                returnOrderInfo.setReturnMsg("操作失败");
                returnOrderInfo.setReturnCode("error");

                return returnOrderInfo;
            }

            // 设置新订单号
            orders.setOrderId(orderId);

            // 获取退券的状态（其他服务器）
            String status = orderService.getReturnStatus(saleCode);

            if (!"success".equals(status)) {
                // 数据入库
                orders.setTradeStatus(Constants.ORDER_STATUS_FAIL);
                orderRepository.save(orders);

                returnOrderInfo.setReturnMsg("操作失败");
                returnOrderInfo.setReturnCode("error");
                returnOrderInfo.setOrgOrderId(orderId);

                return returnOrderInfo;
            }

            // 设置退单信息
            orders.setTradeStatus(Constants.ORDER_TYPE_RETURN);

            // 保存退单
            orderRepository.save(orders);

            returnOrderInfo.setReturnMsg("success");
            returnOrderInfo.setReturnCode("success");
            returnOrderInfo.setOrgOrderId(orgOrderId);
        } catch (Exception e) {
            returnOrderInfo.setReturnMsg("操作失败");
            returnOrderInfo.setReturnCode("error");
            returnOrderInfo.setOrgOrderId(orgOrderId);
        }

        return returnOrderInfo;
    }

    private ReturnOrderInfo returnOrderValidation(String orgOrderId) {
        ReturnOrderInfo returnOrderInfo = new ReturnOrderInfo();

        if (ObjectUtil.isEmpty(orgOrderId)) {
            returnOrderInfo.setReturnMsg("原订单号不能为空");
            returnOrderInfo.setReturnCode("error");
            returnOrderInfo.setOrgOrderId(orgOrderId);

            return returnOrderInfo;
        }

        // 校验数据是否存在
        Orders orders = orderRepository.findByOrigOrderIdAndIsDelete(orgOrderId, "0");

        if (ObjectUtil.isEmpty(orders)) {
            returnOrderInfo.setReturnMsg("此订单不存在");
            returnOrderInfo.setReturnCode("error");
            returnOrderInfo.setOrgOrderId(orgOrderId);

            return returnOrderInfo;
        }

        return null;
    }

    private CreateOrderInfo orderSave(String groupId, String shopId, String salemanId, String goodsId, String orderAmt, String mobile, String custName, String saleCode) {
        CreateOrderInfo createOrderInfo = new CreateOrderInfo();

        String orderId = orderService.generateOrderId();
        logger.info("----------orderId:" + orderId + "----------");

        try {
            // 1.1 订单信息表装箱
            Orders orders = new Orders();
            orders.setOrderId(orderId);// 订单编号
            orders.setOrderType(Constants.ORDER_TYPE_SELL);// 订单类型
            orders.setTradeTime(DateUtil.toString(new Date(), "yyyyMMddHHmmss")); // 交易时间
            orders.setTradeStatus(Constants.ORDER_STATUS_SUCCESS); // 交易状态
            orders.setSaleCode(saleCode); // 交易码
            orders.setOrigOrderId(orderId);// 原订单号
            orders.setShopId(shopId);// 网店号
            orders.setOrgNumber(groupId); // 所属机构号
            orders.setIsDelete("0");// 是否删除
            orders.setProductId(Integer.parseInt(goodsId));// 商品编号

            // 1.1.2  数据入库
            orderRepository.save(orders);

            logger.info("----------orders入库----------");

            // 2.2.1 订单详情表装箱
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProductNumber(goodsId); // 商品编号
            orderDetail.setProductPrice(Double.parseDouble(orderAmt));// 商品价格
            orderDetail.setCustomerName(custName);// 客户名称
            orderDetail.setCustomerPhone(mobile);// 客户手机号
            orderDetail.setSlaerId(salemanId);// 销售人员id
            orderDetail.setNetNumber(shopId);// 网点编号
            orderDetail.setOrderId(orderId);//

            // 2.2.2 数据入库
            orderDetailRepository.save(orderDetail);

            logger.info("----------oorderDetail入库----------");

            createOrderInfo.setOrderId(orderId);
            createOrderInfo.setReturnCode("success");
            createOrderInfo.setReturnMsg(saleCode);

            // 3.成功后商品数量减一
            Product product = productRepository.findByProductNumber(goodsId);
            Integer number = product.getProductAmount() - 1;
            product.setProductAmount(number);
            productRepository.save(product);

            logger.info("----------成功后商品数量减1----------");


            // 4.短信提醒（手机号码，模板，内容）
            String content = "尊敬的客户" + custName + "，购买的" + product.getProductName() + "，券码" + saleCode + "。";
            logger.info("----------短信内容:" + content + ";----------");

            String smsResult = SmsUtil.send(mobile, "10051");

            logger.info("----------手机号码: " + mobile + "; 短信结果：" + smsResult + "----------");

        } catch (Exception e) {
            createOrderInfo.setReturnCode("error");
            createOrderInfo.setReturnMsg("操作失败");
        }

        return createOrderInfo;
    }


    private CreateOrderInfo createOrderValidation(String groupId, String shopId, String salemanId, String goodsId, String orderAmt, String mobile, String custName) {
        CreateOrderInfo createOrderInfo = new CreateOrderInfo();

        if (ObjectUtil.isEmpty(groupId)) {
            createOrderInfo.setReturnCode("error");
            createOrderInfo.setReturnMsg("机构号不能为空");

            return createOrderInfo;
        }

        if (ObjectUtil.isEmpty(shopId)) {
            createOrderInfo.setReturnCode("error");
            createOrderInfo.setReturnMsg("门店号不能为空");

            return createOrderInfo;
        }

        if (ObjectUtil.isEmpty(salemanId)) {
            createOrderInfo.setReturnCode("error");
            createOrderInfo.setReturnMsg("销售人员号不能为空");

            return createOrderInfo;
        }

        if (ObjectUtil.isEmpty(goodsId)) {
            createOrderInfo.setReturnCode("error");
            createOrderInfo.setReturnMsg("商品编号不能为空");

            return createOrderInfo;
        }

        if (ObjectUtil.isEmpty(orderAmt)) {
            createOrderInfo.setReturnCode("error");
            createOrderInfo.setReturnMsg("订单金额不能为空");

            return createOrderInfo;
        }

        if (ObjectUtil.isEmpty(mobile)) {
            createOrderInfo.setReturnCode("error");
            createOrderInfo.setReturnMsg("客户手机号不能为空");

            return createOrderInfo;
        }

        if (ObjectUtil.isEmpty(custName)) {
            createOrderInfo.setReturnCode("error");
            createOrderInfo.setReturnMsg("客户姓名不能为空");

            return createOrderInfo;
        }

        return null;
    }
/*
    public static void main(String[] args) {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
//        org.apache.cxf.endpoint.Client client = dcf.createClient("http://10.250.3.16:9002/chexiang/orders/orders?wsdl");
        org.apache.cxf.endpoint.Client client = dcf.createClient("http://127.0.0.1:8080/orders/orders?wsdl");
        org.apache.cxf.endpoint.Endpoint endpoint = client.getEndpoint();

        String operation = "returnOrder";
        QName opName = new QName(endpoint.getService().getName().getNamespaceURI(), operation);
        BindingInfo bindingInfo = endpoint.getEndpointInfo().getBinding();

        if (bindingInfo.getOperation(opName) == null) {
            for (BindingOperationInfo operationInfo : bindingInfo.getOperations()) {
                if (operation.equals(operationInfo.getName().getLocalPart())) {
                    opName = operationInfo.getName();
                    break;
                }
            }
        }
        Object[] objects = null;

        try {
//            objects = client.invoke(opName, new Object[]{"1", "2", "3", "4", "5", "15021162660", "7"});
//            objects = client.invoke(opName, new Object[]{"89710002", "8971000201", "0001", "1000200001", "200", "wu", "13916847574"});
            objects = client.invoke(opName, new Object[]{"2017062300000000"});
            System.out.println(objects[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}