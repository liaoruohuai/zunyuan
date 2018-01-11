package com.learning.chepei.controller;

import com.learning.chepei.PageModel;
import com.learning.chepei.ResponseCode;
import com.learning.chepei.SessionData;
import com.learning.chepei.controller.impl.SellCardServiceImpl;
import com.learning.chepei.util.SSLKit;
import com.learning.login.entity.Member;
import com.learning.login.entity.Saler;
import com.learning.login.service.MemberService;
import com.learning.order.entity.Apply;
import com.learning.order.service.ApplyService;
import com.learning.order.service.OrderService;
import com.learning.util.basic.Constants;
import com.learning.util.basic.ObjectUtil;
import com.learning.util.basic.TxnSequence;
import com.learning.util.basic.ValueUtil;
import com.learning.util.comm.RanKit;
import com.learning.util.date.DateUtil;
import com.learning.util.encryption.MD5;
import com.learning.util.exception.HzbuviException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/6.
 */
@RestController
@RequestMapping("/apply")
public class ApplyController {
    @Autowired
    private ApplyService applyService;

    @Autowired
    private MemberService memberService;

    /**
     * 员工售卡接口
     * @param apply
     * @param response
     * @return
     */
    @RequestMapping("/memberApplyCard")
    public String memberApplyCard(Apply apply, HttpServletResponse response) {
        try {
            String searchResult = applyService.memberApply(apply);
            return ValueUtil.toJson("status", searchResult);
        }catch (Exception e) {
            return ValueUtil.toError(e.getMessage(),"");
        }
    }

    /**
     * 员工售卡接口
     * @param apply
     * @param response
     * @return
     */
    @RequestMapping("/SalerSellCard")
    public String salerSellCard(Apply apply, HttpServletResponse response){
        try {
            String searchResult = applyService.findOldApply(apply);
            if (!searchResult.equals("NewCard")){
                return ValueUtil.toJson("status", searchResult);
            }

            Apply resultApply = applyService.salerInitApply(apply);
            String message = SellCardServiceImpl.getXMLReq(resultApply);

            System.out.println("售卡请求报文：+ [" + message + "]");
            String result = SellCardServiceImpl.getXMLResp(SSLKit.sslSend(message));
            //String result = SSLKit.socketSend(message);


            resultApply.setApplyResp(result);
            if (!applyService.updateApplyBySaler(resultApply).equals("updateSucc")) {
                return ValueUtil.toJson("status", "updateFail");
            }
            if (!ObjectUtil.isEmpty(result)&&result.equals("0000")) {
                Member newMember = new Member();
                newMember.setMemberName(resultApply.getName());
                newMember.setMemberPhone(resultApply.getMobile());
                newMember.setMemberPwd(MD5.getMD5(resultApply.getMobile().substring(5)));
                newMember.setMemberCertType("1");
                newMember.setMemberCertNo(resultApply.getIdNum());
                newMember.setIsInitPwd("0");
                newMember.setMemberLevel("2");
                newMember.setMemberPoint("0");
                newMember.setRegistTime(DateUtil.toString(new Date(),"yyyyMMddHHmmss"));

                memberService.insert(newMember);
                if (ObjectUtil.isEmpty(newMember.getMemberId())) {
                    System.out.println("售卡新增会员失败");
                }
            }

            if (!ObjectUtil.isEmpty(result)){
                if (result.equals("0000")) {
                    return ValueUtil.toJson("status", "success");
                } else {
                    return ValueUtil.toJson("status", ResponseCode.getMessageByCode(result));
                }
            }else
            {
                return ValueUtil.toJson("status", "sslfail");
            }

        } catch (Exception e) {
            return ValueUtil.toError(e.getMessage(),"");
        }
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam Map<String,String> parm, HttpServletResponse response){
        Map<String,Object>  result = applyService.show(parm);
        PageModel pageModel = new PageModel((Page) result.get("page"));
        pageModel.setCondition(result.get("condition"));
        return  ValueUtil.toJson("apply",pageModel);
    }

    @RequestMapping(value = "getSalerApplyList")
    public String getSalerApplyList(HttpServletRequest request, HttpServletResponse response){
        try {
            response.setHeader("Access-Control-Allow-Origin", Constants.frontManageUrl);
            String salerId = SessionData.verify(request,response);
            return ValueUtil.toJson("applyList",applyService.getSalerApplyList(salerId));
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }

    /**
     * 根据会员id查找申请(APP)
     * @param request
     * @return
     */
    @RequestMapping("/findMemberApply")
    public String findMember(HttpServletRequest request, HttpServletResponse response){
        try {
            Integer memberId = Integer.parseInt(SessionData.verify(request,response));
            return ValueUtil.toJson("applys",applyService.findMemberApply(memberId));
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }
}
