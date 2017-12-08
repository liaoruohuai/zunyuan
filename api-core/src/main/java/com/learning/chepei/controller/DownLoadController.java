package com.learning.chepei.controller;

import com.learning.downLoad.service.DownLoadService;
import com.learning.util.basic.Constants;
import com.learning.util.date.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;


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
    public void applyDownLoad(@RequestParam Map<String, String> param, HttpServletResponse response){
        try {
//            response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
            HSSFWorkbook wkb = downLoadService.applyDownLoad(param);
            OutputStream output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=客户申请"+ DateUtil.toString(new Date(),"yyyymmdd")+".xls");
            response.setContentType("application/msexcel");
            wkb.write(output);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
