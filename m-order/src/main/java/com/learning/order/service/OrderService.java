package com.learning.order.service;

//import com.hzbuvi.login.entity.Saler;
//import com.hzbuvi.login.repository.SalerRepository;
//import OrderDetail;
//import OrderDetailRepository;
//import Product;

import com.google.gson.Gson;
import com.learning.order.beans.CXJCouponVO;
import com.learning.order.entity.Orders;
import com.learning.product.entity.Product;
import com.learning.product.repository.ProductRepository;
import com.learning.order.repository.OrderRepository;
import com.learning.util.basic.Constants;
import com.learning.util.basic.ObjectUtil;
import com.learning.util.basic.ValueUtil;
import com.learning.util.date.DateUtil;
import com.learning.util.exception.HzbuviException;
import org.apache.commons.httpclient.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:GR
 * Date:2016/11/10
 * Discription:
 */
@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    /*@Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private SalerRepository salerRepository;*/
    @Autowired
    private ProductRepository productRepository;

    private static final String CHANNEL = "petro_china";
    private static final String ORDER_STATUS_SUCCESS = "0000";
    private static final String ORDER_STATUS_FAIL = "1111";
    private static int defaultPageSize = 10;

    /**
     * 售券
     *
     * @param productId
     * @param pruductPrice
     * @param customerName
     * @param customerPhone
     * @return
     * @throws HzbuviException
     */
    public String sellProduct(Integer userId, String productId, String pruductPrice, String customerName, String customerPhone) throws HzbuviException {
        Product product = productRepository.findOne(Integer.parseInt(productId));
        if (ObjectUtil.isEmpty(product) || product.getProductAmount() == 0) {
            return ValueUtil.toJson("status", "fail", "message", "产品已经卖完，请联系管理员！");
        }
        Orders order = new Orders();
        setOrderParam(order);
        order.setOrderType(Constants.ORDER_TYPE_SELL);
        order.setProductId(Integer.parseInt(productId));
        String sellCode = getSellCode(pruductPrice, customerName, customerPhone, product.getCouponPoolNo());
        if (StringUtils.isEmpty(sellCode)) {
            order.setTradeStatus(ORDER_STATUS_FAIL);
            orderRepository.save(order);
            return ValueUtil.toJson("status", "fail", "message", "售卖失败！");
        }
        order.setSaleCode(sellCode);
        order.setTradeStatus(ORDER_STATUS_SUCCESS);
        orderRepository.save(order);
        saveProduct(Integer.parseInt(productId), -1);//成功后商品数量减一
//        saveOrdetail(userId,generateOrderId(),productId,pruductPrice,customerName,customerPhone);
        return ValueUtil.toJson("status", "success", "sellCode", sellCode);
    }

    /**
     * 商品数量减一，保存
     *
     * @param productId
     * @param n
     */
    public void saveProduct(Integer productId, Integer n) {
        Product product = productRepository.findOne(productId);
        Integer number = product.getProductAmount() + n;
        product.setProductAmount(number);
        productRepository.save(product);
    }

    /**
     * 退券
     *
     * @param saleCode
     * @return
     */
    public String returnProduct(String saleCode) throws HzbuviException {
        Orders order = new Orders();
        setOrderParam(order);
        order.setOrderType(Constants.ORDER_TYPE_RETURN);
        order.setSaleCode(saleCode);
        Orders saleOrder = orderRepository.findByOrderTypeAndSaleCode(Constants.ORDER_TYPE_SELL, saleCode);
        if (ObjectUtil.isEmpty(saleOrder)) {
            return ValueUtil.toJson("status", "fail");
        }
        Integer productId = saleOrder.getProductId();
        order.setProductId(productId);
        String status = getReturnStatus(saleCode);
        if (!"success".equals(status)) {
            order.setTradeStatus(ORDER_STATUS_FAIL);
            orderRepository.save(order);
            return ValueUtil.toJson("status", "fail");
        }
        order.setTradeStatus(ORDER_STATUS_SUCCESS);
        orderRepository.save(order);
        Orders orders = orderRepository.findByOrderTypeAndSaleCode("1000", saleCode);
        orders.setTradeStatus(ORDER_STATUS_FAIL);
        orderRepository.save(orders);
        saveProduct(productId, 1);
        return ValueUtil.toJson("status", "success");
    }

    /**
     * 设置订单通用参数
     *
     * @param order
     * @return
     */
    public Orders setOrderParam(Orders order) {
        order.setOrderId(generateOrderId());
        order.setTradeTime(DateUtil.toString(new Date(), "yyyyMMddHHmmss"));
        order.setOrigOrderId(generateOrderId());
        order.setIsDelete("0");
        return order;
    }

    /**
     * 保存订单详情
     * @param userId
     * @param orderId
     * @param productId
     * @param pruductPrice
     * @param customerName
     * @param customerPhone
     */
//    private void saveOrdetail(Integer userId,String orderId,String productId,String pruductPrice,String customerName,String customerPhone){
//        Saler saler = salerRepository.findOne(String.valueOf(userId));
//        Product product = productRepository.findOne(Integer.parseInt(productId));
//        OrderDetail orderDetail=new OrderDetail();
//        orderDetail.setOrderId(orderId);
//        orderDetail.setSalerName(saler.getSalerName());
//        orderDetail.setCustomerName(customerName);
//        orderDetail.setCustomerPhone(customerPhone);
//        orderDetail.setNetNumber(saler.getNetNumber());
//        orderDetail.setSlaerId(saler.getSalerId());
//        orderDetail.setProductName(product.getProductName());
//        orderDetail.setProductNumber(product.getProductNumber());
//        orderDetail.setProductPrice(Double.valueOf(pruductPrice));
//        orderDetailRepository.save(orderDetail);
//    }

    /**
     * 获取销售码
     *
     * @param pruductPrice
     * @param customerName
     * @param customerPhone
     * @return
     */
    public String getSellCode(String pruductPrice, String customerName, String customerPhone, String couponPoolNo) {
        try {
            // 测试地址
            //String url = "http://openapi.pre.chexiang.com/services/cXJCouponServiceApi/saveCouponSalesResult?" + "channel=" + CHANNEL
              //      + "&couponPoolNo=" + couponPoolNo + "&salesAmt=" + pruductPrice + "&receiveAmt=" + pruductPrice + "&mobile="
                //    + customerPhone + "&custName=" + customerName;

            // 生产地址
            String url = "http://10.100.84.103:9186/services/cXJCouponServiceApi/saveCouponSalesResult?" + "channel=" + CHANNEL
                    + "&couponPoolNo=" + couponPoolNo + "&salesAmt=" + pruductPrice + "&receiveAmt=" + pruductPrice + "&mobile="
                    + customerPhone + "&custName=" + customerName;

            HttpClient httpClient = new HttpClient();
            String service = "cXJCouponServiceApi";
            String method = "saveCouponSalesResult";
            String message = setSellMessage(pruductPrice, customerName, customerPhone, couponPoolNo);
            String result = HttpClientUtil.setHttpHead(httpClient, url, service, method, message);
            return parseJson(result);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置售券的message
     *
     * @param pruductPrice
     * @param customerName
     * @param customerPhone
     * @param couponPoolNo
     * @return
     */
    private String setSellMessage(String pruductPrice, String customerName, String customerPhone, String couponPoolNo) {
        CXJCouponVO query = new CXJCouponVO();
        query.setChannel(CHANNEL);
        query.setCouponPoolNo(couponPoolNo);
        query.setSalesAmt(pruductPrice);
        query.setReceiveAmt(pruductPrice);
        query.setMobile(customerPhone);
        query.setCustName(customerName);
        Map<String, Object> map = new HashMap<>();
        map.put("couponSalesVo", query);
        return new Gson().toJson(map);
    }

    /**
     * 设置退券的message
     *
     * @param couponNo
     * @return
     */
    private String setReturnMessage(String couponNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("couponNo", couponNo);
        map.put("channel", CHANNEL);
        return new Gson().toJson(map);
    }

    /**
     * 获取退券的状态
     *
     * @param saleCode
     * @return
     */
    public String getReturnStatus(String saleCode) {
        try {
            // 生产地址
//            String url = "http://openapi.chexiang.com/services/cXJCouponServiceApi/backCouponResult?"
//                    + "couponNo=" + saleCode + "&channel=" + CHANNEL;
            // 测试地址
            //String url = "http://openapi.pre.chexiang.com/services/cXJCouponServiceApi/backCouponResult?" + "couponNo=" + saleCode + "&channel=" + CHANNEL;
            String url = "http://10.100.84.103:9187/services/cXJCouponServiceApi/backCouponResult?" + "couponNo=" + saleCode + "&channel=" + CHANNEL;
            HttpClient httpClient = new HttpClient();
            String service = "cXJCouponServiceApi";
            String method = "backCouponResult";
            String message = setReturnMessage(saleCode);
            String result = HttpClientUtil.setHttpHead(httpClient, url, service, method, message);
            return parseJson(result);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析json
     *
     * @param json
     * @return
     * @throws JSONException
     */
    private String parseJson(String json) throws JSONException {
        JSONObject jsonObj = new JSONObject(json);
        String errorCode = jsonObj.get("errorCode").toString();
        if (!errorCode.equals("0")) {
            return null;
        }
        JSONObject result = jsonObj.getJSONObject("result");
        String returnCode = result.getString("returnCode");
        if (!returnCode.equals("success")) {
            return null;
        }
        return result.getString("returnMsg");
    }

    /**
     * 生成订单Id
     *
     * @return
     */
    public String generateOrderId() {
        String now = DateUtil.toString(new Date(), "yyyyMMdd");
        String tomorrow = DateUtil.toString(DateUtil.getPreOrNextDate(new Date(), 1), "yyyyMMdd");
        List<Orders> orders = orderRepository.findByTradeTimeGreaterThanEqualAndTradeTimeLessThan(now, tomorrow);
        return now + generateSerial(orders);
    }

    private String generateSerial(List<Orders> list) {
        if (ObjectUtil.isEmpty(list) || list.size() == 0) {
            return "00000000";
        }
        String max = list.stream().map(orders -> orders.getOrderId()).max(String::compareTo).get();
        String size = String.valueOf(Long.parseLong(max) + 1);
        Integer length = size.length();
        String str = "00000000" + size;
        return str.substring(length, 8 + length);
    }

    /**
     * 查询订单
     *
     * @param parm
     * @return
     */
    public Map<String, Object> show(Map<String, String> parm) {
        String page = ValueUtil.coalesce(parm.get("page"), "0");
        String orderId = parm.get("orderId");
        String orderType = parm.get("orderType");
        String saleCode = parm.get("saleCode");
        Map<String, String> map1 = new HashMap<>();
        if (ValueUtil.notEmpity(orderId)) {
            map1.put("orderId", orderId);
        }
        if (ValueUtil.notEmpity(orderType)) {
            map1.put("orderType", orderType);
        }
        if (ValueUtil.notEmpity(saleCode)) {
            map1.put("saleCode", saleCode);
        }
        //new Sort(Direction.DESC, new String[] { "id" }
        Pageable pageable = new PageRequest(Integer.valueOf(page), defaultPageSize, new Sort(Sort.Direction.DESC, new String[]{"orderId"}));
        Page<Orders> orderses = orderRepository.findAll(getWhereClause(orderId, orderType, saleCode), pageable);
        Map<String, Object> map = new HashMap<>();
        map.put("page", orderses);
        map.put("condition", map1);
        return map;
    }

    private Specification<Orders> getWhereClause(String orderId, String orderType, String saleCode) {
        return new Specification<Orders>() {
            @Override
            public Predicate toPredicate(Root<Orders> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                if (ValueUtil.notEmpity(orderId)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("orderId"), "%" + orderId + "%"));
                }
                if (ValueUtil.notEmpity(orderType)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("orderType"), "%" + orderType + "%"));
                }
                if (ValueUtil.notEmpity(saleCode)) {
                    predicate.getExpressions().add(cb.like(root.<String>get("saleCode"), "%" + saleCode + "%"));
                }
                predicate.getExpressions().add(cb.equal(root.<String>get("isDelete"), "0"));
                query.where(predicate);
                //query.orderBy(cb.desc(root.get("gmtCreate").as(Date.class)));
                query.orderBy(cb.desc(root.<String>get("orderId")));
                return predicate;
            }
        };
    }


}
