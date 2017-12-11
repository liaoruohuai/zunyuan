package com.learning.chepei.controller;

import com.learning.chepei.PageModel;
import com.learning.chepei.SessionData;
import com.learning.chepei.util.SmsKit;
import com.learning.login.entity.Saler;
import com.learning.login.entity.User;
import com.learning.login.service.LoginService;
import com.learning.login.service.SmsLogService;
import com.learning.util.basic.Constants;
import com.learning.util.basic.ObjectUtil;
import com.learning.util.basic.ValueUtil;
import com.learning.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Author:GR
 * Date:2016/11/9
 * Discription:
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private SmsLogService smsLogService;

    /**
     * 前台登录接口
     * @param saler
     * @param response
     * @return
     */
    @RequestMapping("/front")
    public String frontLogin(Saler saler, HttpServletResponse response){
        try {
            Integer id=Integer.parseInt(loginService.frontLogin(saler));

            if (id!=-1) {
                SessionData.login(response, String.valueOf(id));
         /*   String fSession = Rdata.timeRandomCode(10);
            SessionData.put(fSession,id);
            response.setHeader("F-Session",fSession);
            response.setHeader("Access-Control-Expose-Headers","F-Session");*/
                return ValueUtil.toJson("status", "success");
            }else
            {
                return ValueUtil.toJson("status", "wrongpwd");
            }
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }

    /**
     * 根据用户id查找用户(APP)
     * @param request
     * @return
     */
    @RequestMapping("/find_saler")
    public String findSaler(HttpServletRequest request, HttpServletResponse response){
        try {
            String salerId = SessionData.verify(request,response);
            return ValueUtil.toJson("salers",loginService.findSaler(salerId));
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }



    /**
     * 前台登出接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout_front",method = RequestMethod.POST)
    public String logoutFront(HttpServletRequest request,HttpServletResponse response){
        try {
//            SessionData.destroyFront(request);
 //           response.setHeader("Access-Control-Allow-Origin", Constants.frontManageUrl);
            return ValueUtil.toJson("status","success");
        } catch (Exception e) {
            return ValueUtil.toJson("status","fail");
        }
    }

    /**
     * 后台登录接口
     * @param user
     * @param response
     * @return
     */
    @RequestMapping("/back")
    public String backLogin(User user, HttpServletResponse response){
        try {
     //       response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
            if (ObjectUtil.isEmpty(user.getLoginName()) ||ObjectUtil.isEmpty(user.getPassword())){
                return ValueUtil.toJson("status","error","message","用户名或密码不能为空！");
            }
            User result = loginService.backLogin(user);
            if (ObjectUtil.isEmpty(result)){
                return ValueUtil.toJson("status","error","message","用户名或密码错误！");
            }
            Integer id=result.getUserId();
            SessionData.loginBack(response,String.valueOf(id));
            /*String xSession = Rdata.timeRandomCode(10);
            SessionData.put(xSession,id);
            response.setHeader("X-Session",xSession);
            response.setHeader("Access-Control-Expose-Headers","X-Session");*/
            return ValueUtil.toJson("status","success","message","");
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }

    /**
     * 后台登出接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public String logout(HttpServletRequest request,HttpServletResponse response){
//        SessionData.destroy(request);
  //      response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
        return ValueUtil.toJson("status","success");
    }

    /**
     * 销售人员信息分页查询
     * @param parm
     * @return
     */
    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show(@RequestParam Map<String,String> parm,HttpServletRequest request,HttpServletResponse response){
        try {
            SessionData.verifyBack(request,response);
            Map<String,Object>  result = loginService.show(parm);
            PageModel pageModel = new PageModel((Page) result.get("page"));
            pageModel.setCondition(result.get("condition"));
            return  ValueUtil.toJson("sales",pageModel);
        } catch (HzbuviException e) {
            return ValueUtil.toError(e.getCode(),"");
        }
    }

    /**
     * 验证码发送接口
     * @param
     * @return
     */
    @RequestMapping(value = "/smsValid")
    public String smsLog(Saler saler,HttpServletResponse response){
        try {
            String validNum = smsLogService.genValidNum();
            String phoneNum = saler.getSalerPhone();
            String smsContent = "本次验证码: " + validNum + " 有效时间60秒,请注意保密";
            String result = SmsKit.send(phoneNum, smsContent);
            smsLogService.saveSmsLog(phoneNum, smsContent, result);
            SessionData.validNum(response, validNum);
            return ValueUtil.toJson("status", "success");
        }
        catch (HzbuviException e) {
            return ValueUtil.toError(e.toString(),e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            return ValueUtil.toError(e.toString(),e.getMessage());
        } catch (UnsupportedEncodingException e) {
            return ValueUtil.toError(e.toString(),e.getMessage());
        }
        /*
        catch (UnsupportedEncodingException e) {
            return ValueUtil.toError(e.toString(),e.getMessage());
        }*/
    }

    /**
     * 员工密码重置接口
     * @param
     * @return
     */
    @RequestMapping(value = "/salerReset")
    public String Reset_saler(Saler saler,HttpServletRequest request){
        try {
            String oldValidNum=SessionData.verifyValidNum(request,saler.getSalerName());
            if (oldValidNum.equals("ValidSucc")){
                String result=loginService.salerResetPwd(saler);
                if (result.equals("ResetSucc")){
                    return ValueUtil.toJson("status", "success");
                }else{
                    return ValueUtil.toJson("status", "setPwdfail");
                }
            }else
            {
                return ValueUtil.toJson("status", "validDiff");
            }
        }
        catch (Exception e){return ValueUtil.toError(e.toString(),e.getMessage());}
        /*
        catch (HzbuviException e) {
            return ValueUtil.toError(e.toString(),e.getMessage());
        }*/

    }
}
