package com.learning.chepei.controller;

import com.learning.chepei.SessionData;
import com.learning.login.entity.Saler;
import com.learning.login.service.LoginService;

import com.learning.product.entity.Product;
import com.learning.util.basic.Constants;
import com.learning.util.basic.ValueUtil;
import com.learning.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Author:GR
 * Date:2016/11/10
 * Discription:销售人员controller
 */
@RestController
@RequestMapping("/saler")
public class SalerController {
    @Autowired
    private LoginService loginService;

    @RequestMapping("/saler")
    public String loadSaler(HttpServletRequest request, HttpServletResponse response){
        try {
            Integer userId = Integer.valueOf(SessionData.verify(request,response));
            return ValueUtil.toJson("salerLoad",loginService.loadSaler(userId));
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String create(Saler saler, HttpServletRequest request, HttpServletResponse response){
        try {
            SessionData.verifyBack(request,response);
            return  ValueUtil.toJson("saler",loginService.insert(saler));
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(String id,HttpServletResponse response){
        //      response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
        return ValueUtil.toJson("SalesPerson",loginService.index(id));
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public String destroy( String netNumber,HttpServletResponse response) {

        return ValueUtil.toJson("SalesPerson", loginService.delete(netNumber));
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public String update(Saler saler,HttpServletResponse response) {
        return ValueUtil.toJson("SalesPerson", loginService.update(saler));

    }
}
