package com.learning.chepei.controller;

import com.learning.chepei.PageModel;
import com.learning.chepei.SessionData;
import com.learning.order.service.OrderService;
import com.learning.util.basic.Constants;
import com.learning.util.basic.ValueUtil;
import com.learning.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Author:GR
 * Date:2016/11/10
 * Discription:订单controller
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("/sell")
    public String sellProduct(HttpServletRequest request, HttpServletResponse response, String productId, String pruductPrice, String customerName, String customerPhone){
        try {
            Integer userId = Integer.valueOf(SessionData.verify(request,response));
            return orderService.sellProduct(userId,productId,pruductPrice,customerName,customerPhone);
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }

    @RequestMapping("/return")
    public String returnProduct(HttpServletRequest request, HttpServletResponse response,String saleCode){
        try {
            SessionData.verify(request,response);
            return orderService.returnProduct(saleCode);
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam Map<String,String> parm,HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
        Map<String,Object>  result = orderService.show(parm);
        PageModel pageModel = new PageModel((Page) result.get("page"));
        pageModel.setCondition(result.get("condition"));
        return  ValueUtil.toJson("order",pageModel);
    }
}
