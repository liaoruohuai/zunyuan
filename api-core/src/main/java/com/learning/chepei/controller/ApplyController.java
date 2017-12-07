package com.learning.chepei.controller;

import com.learning.chepei.PageModel;
import com.learning.order.service.ApplyService;
import com.learning.order.service.OrderService;
import com.learning.util.basic.ValueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/6.
 */
@RestController
@RequestMapping("/apply")
public class ApplyController {
    @Autowired
    private ApplyService applyService;

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam Map<String,String> parm, HttpServletResponse response){
        Map<String,Object>  result = applyService.show(parm);
        PageModel pageModel = new PageModel((Page) result.get("page"));
        pageModel.setCondition(result.get("condition"));
        return  ValueUtil.toJson("apply",pageModel);
    }
}
