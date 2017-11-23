package com.learning.chepei.controller;

import com.learning.chepei.SessionData;
import com.learning.login.service.LoginService;
import com.learning.util.basic.ValueUtil;
import com.learning.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
