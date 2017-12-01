package com.learning.chepei.controller;

import com.learning.chepei.PageModel;
import com.learning.chepei.SessionData;
import com.learning.org.entity.OrgEntity;
import com.learning.org.service.OrgService;
import com.learning.util.basic.Constants;
import com.learning.util.basic.ValueUtil;
import com.learning.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by liqingqing on 2016/11/11.
 */
@RestController
@RequestMapping(value = "/org")
public class OrgController {
    @Autowired
    private OrgService orgService;

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String create(OrgEntity orgEntity, HttpServletResponse response){
 //       response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
        //System.out.println(orgEntity.getOrgName());
        return  ValueUtil.toJson("org",orgService.insert(orgEntity));
    }
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public String destroy(String orgNumber,HttpServletResponse response) {
 //       response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
        return ValueUtil.toJson("org", orgService.delete(orgNumber));
    }
    @RequestMapping(value = "/orgUpdate",method = RequestMethod.POST)
    public String update(OrgEntity orgEntity,HttpServletResponse response) {
 //       response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
        return ValueUtil.toJson("org", orgService.update(orgEntity));

    }
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam Map<String,String> parm,HttpServletResponse response){
 //       response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
        Map<String,Object>  result = orgService.show(parm);
        PageModel pageModel = new PageModel((Page) result.get("page"));
        pageModel.setCondition(result.get("condition"));
        return  ValueUtil.toJson("org",pageModel);
    }

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(@RequestParam Integer id, HttpServletResponse response, HttpServletRequest request){
        try {
            SessionData.verifyBack(request,response);
            return ValueUtil.toJson("OrgEntity",orgService.index(id));
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }
}
