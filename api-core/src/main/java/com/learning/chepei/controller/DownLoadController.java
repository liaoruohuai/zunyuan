package com.learning.chepei.controller;

import com.learning.downLoad.service.DownLoadService;
import com.learning.util.basic.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


/**
 * Author:GR
 * Date:2016/11/10
 * Discription:下载的controller
 */
@RestController
@RequestMapping("/downLoad")
public class DownLoadController {
    @Autowired
    private DownLoadService downLoadService;

    /**
     * 下载销售人员信息
     * @param response
     */
    @RequestMapping("/saler")
    public void salerDownLoad(HttpServletResponse response){
        try {
  //          response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
            downLoadService.salerDownLoad(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载订单信息
     * @param response
     */
    @RequestMapping("/order")
    public void orderDownLoad(HttpServletResponse response){
        try {
//            response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
            downLoadService.orderDownLoad(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 下载订单信息
     * @param response
     */
    @RequestMapping("/apply")
    public void applyDownLoad(HttpServletResponse response){
        try {
//            response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
            downLoadService.applyDownLoad(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
