package com.learning.chepei.controller;

import com.learning.chepei.PageModel;
import com.learning.login.entity.Member;
import com.learning.login.service.MemberService;
import com.learning.util.basic.ObjectUtil;
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
 * Created by Administrator on 2017/12/12.
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam Map<String,String> parm, HttpServletResponse response){
        Map<String,Object>  result = memberService.show(parm);
        PageModel pageModel = new PageModel((Page) result.get("page"));
        pageModel.setCondition(result.get("condition"));
        return  ValueUtil.toJson("member",pageModel);
    }

    /**
     * 会员信息修改接口
     * @param member
     * @param response
     * @return
     */
    @RequestMapping("/updateMemberInfo")
    public String updateMemberInfo(Member member, HttpServletResponse response){
        try {
            String result = memberService.updateMemberInfo(member);
            return ValueUtil.toJson("status", result);
        } catch (Exception e) {
            return ValueUtil.toError(e.getMessage(),"");
        }
    }
}
