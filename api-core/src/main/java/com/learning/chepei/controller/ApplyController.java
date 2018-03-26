package com.learning.chepei.controller;

import com.learning.chepei.PageModel;
import com.learning.chepei.ResponseCode;
import com.learning.chepei.SessionData;
import com.learning.chepei.controller.impl.SellCardServiceImpl;
import com.learning.chepei.util.SSLKit;
import com.learning.chepei.util.SmsKit;
import com.learning.login.entity.Member;
import com.learning.login.entity.Saler;
import com.learning.login.service.MemberService;
import com.learning.login.service.SmsLogService;
import com.learning.order.entity.Apply;
import com.learning.order.entity.Coupon;
import com.learning.order.service.ApplyService;
import com.learning.order.service.CouponService;
import com.learning.order.service.OrderService;
import com.learning.util.basic.Constants;
import com.learning.util.basic.ObjectUtil;
import com.learning.util.basic.TxnSequence;
import com.learning.util.basic.ValueUtil;
import com.learning.util.comm.RanKit;
import com.learning.util.date.DateUtil;
import com.learning.util.encryption.MD5;
import com.learning.util.exception.HzbuviException;
import com.sun.xml.bind.v2.TODO;
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

    @Autowired
    private SmsLogService smsLogService;

    @Autowired
    private CouponService couponService;

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
    public String salerSellCard(Apply apply,HttpServletRequest request, HttpServletResponse response){
        try {
            /*
            String searchResult = applyService.findOldApply(apply);
            if (!searchResult.equals("NewCard")){
                return ValueUtil.toJson("status", searchResult);
            }
            */

            apply.setLastUpdateTime(MD5.getMD5(apply.getLastUpdateTime()));
            String oldValidNum=SessionData.verifyValidNum(request,apply.getLastUpdateTime());
            if (oldValidNum.equals("ValidSucc")){
                //TODO：客户意愿验证成功后，查出资料进行申卡
                Apply resultApply = applyService.findValidApply(apply);
                if (ObjectUtil.isEmpty(resultApply.getApplyId()))
                {
                    //如果出错没找到资料，返回ApplyMiss，界面提示系统出错，联系管理员
                    return ValueUtil.toJson("status", "ApplyMiss");
                }
                String message = SellCardServiceImpl.getXMLReq(resultApply);

                System.out.println("售卡请求报文：+ [" + message + "]");
                String result = SellCardServiceImpl.getXMLResp(SSLKit.sslSend(message));
                //String result = SSLKit.socketSend(message);


                resultApply.setApplyResp(result);
                //resultApply.setInsurance(apply.getInsurance());
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
                    newMember.setMemberCertDate(resultApply.getIdDate());
                    newMember.setIsInitPwd("0");
                    newMember.setMemberLevel("2");
                    newMember.setMemberPoint("0");
                    newMember.setRegistTime(DateUtil.toString(new Date(),"yyyyMMddHHmmss"));

                    String memberfind = memberService.findOldMember(newMember);
                    if (memberfind.equals("NewMember")) {
                        memberService.insert(newMember);
                        if (ObjectUtil.isEmpty(newMember.getMemberId())) {
                            System.out.println("售卡新增会员失败");
                        }
                    }else {
                        System.out.println("售卡新增会员冲突，该手机号已存在会员");
                    }

                    //TODO:售卡成功后的送券
                    Coupon newCoupon = couponService.getOneCoupon(newMember.getMemberPhone());
                    if (ObjectUtil.isEmpty(newCoupon)||ObjectUtil.isEmpty(newCoupon.getCouponInfo())){
                        System.out.println("警告！券已送完，请及时维护");
                    } else {
                        newMember.setIsCouponed(newCoupon.getCouponInfo());
                        memberService.insert(newMember);

                        String smsContent = "温馨提示！您已成功参与本次【办理车享付油卡活动】，您的" + newCoupon.getCouponDesp()  + "券码为："
                                + newCoupon.getCouponInfo() + "，有效期至" + newCoupon.getCouponValidDate() + "，请持该券码在有效期内到店完成服务，过期未到店完成服务将自动失效。本券不可兑换现金，不找零，不与其他活动共享。";
                        String smsResult = SmsKit.send(newMember.getMemberPhone(), smsContent);
                        smsLogService.saveSmsLog(newMember.getMemberPhone(), smsContent, smsResult);
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
            }else {//验证码验证错误
                return ValueUtil.toJson("status", "validDiff");
            }
        } catch (Exception e) {
            return ValueUtil.toError(e.getMessage(),"");
        }
    }

    /**
     * 员工售卡验证客户意愿接口
     * @param apply
     * @param response
     * @return
     */
    @RequestMapping("/wishVerify")
    public String wishVerify(Apply apply, HttpServletResponse response){
        try {
            String searchResult = applyService.findOldApply(apply);
            if (!searchResult.equals("NewCard")){
                return ValueUtil.toJson("status", searchResult);
            }


            String validNum = smsLogService.genValidNum();
            String phoneNum = apply.getMobile();
            String smsContent = "本次验证码: " + validNum + " （请提供给发卡员完成确认,有效时间60秒,请注意保密）。提醒：提供该验证码即视为同意提供您的相关信息以供办理中石油上汽联名卡，如非本人操作请忽略此短信";
            String result = SmsKit.send(phoneNum, smsContent);
            smsLogService.saveSmsLog(phoneNum, smsContent, result);
            SessionData.validNum(response, validNum);

            apply.setLastUpdateTime(MD5.getMD5(validNum));//暂时借用该字段，存储本次验证码，申请完成后会覆盖成时间
            Apply resultApply = applyService.salerInitApply(apply);

            return ValueUtil.toJson("status", "success");

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
