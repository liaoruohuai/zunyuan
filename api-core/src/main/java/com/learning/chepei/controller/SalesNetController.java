package com.learning.chepei.controller;

import com.learning.chepei.PageModel;
import com.learning.salesNetwork.entity.SalesNetwork;
import com.learning.salesNetwork.service.SalesNetService;
import com.learning.util.basic.Constants;
import com.learning.util.basic.ValueUtil;
import com.learning.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Author:GR
 * Date:2016/11/9
 * Discription:销售网点controller
 */
@RestController
@RequestMapping("/salesNetwork")
public class SalesNetController {
    @Autowired
    private SalesNetService salesNetService;

    @RequestMapping("/load")
    public String loadSalesNetwork(HttpServletResponse response){
        try {
 //           response.setHeader("Access-Control-Allow-Origin", Constants.frontManageUrl);
            return ValueUtil.toJson("saleNet",salesNetService.loadSalesNetwork());
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }

    /**
     * 添加
     * @param salesNetwork
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String create(SalesNetwork salesNetwork,HttpServletResponse response){
     //   response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
        return  ValueUtil.toJson("salesNetwork",salesNetService.insert(salesNetwork));
    }

    /**
     * 删除
     * @param netNumber
     * @return
     */
    /*@RequestMapping(value = "/{netNumber}",method = RequestMethod.DELETE)
    public String destroy(@PathVariable("netNumber") String netNumber,HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
        return ValueUtil.toJson("salesNetwork", salesNetService.delete(netNumber));

    }*/

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public String destroy( String netNumber,HttpServletResponse response) {
        return ValueUtil.toJson("salesNetwork", salesNetService.delete(netNumber));

    }

    /**
     * 更新
     * @param salesNetwork
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public String update(SalesNetwork salesNetwork,HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
        return ValueUtil.toJson("salesNetwork", salesNetService.update(salesNetwork));

    }

    /**
     * 查看
     * @param parm
     * @return
     */
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam Map<String,String> parm,HttpServletResponse response){
        Map<String,Object>  result = salesNetService.show(parm);
        PageModel pageModel = new PageModel((Page) result.get("page"));
        pageModel.setCondition(result.get("condition"));
        return  ValueUtil.toJson("organizationUser",pageModel);
    }

    /**
     * 根据Id查询网点记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(Integer id,HttpServletResponse response){
  //      response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
        return ValueUtil.toJson("SalesNetwork",salesNetService.index(id));
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test(){
//        response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
        return ValueUtil.toJson("SalesNetwork","sucess");
    }
}
