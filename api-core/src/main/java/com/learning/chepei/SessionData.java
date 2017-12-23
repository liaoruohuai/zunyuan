package com.learning.chepei;

import com.learning.util.basic.ObjectUtil;
import com.learning.util.exception.HzbuviException;
import com.learning.util.encryption.MD5;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by WANG, RUIQING on 10/18/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */
public class SessionData {

    public static String verify(HttpServletRequest request, HttpServletResponse response) throws HzbuviException {
        //response.setHeader("Access-Control-Allow-Origin", Constants.frontManageUrl);
        Cookie[] cookies = request.getCookies();
        String userCode = "";
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                switch (cookies[i].getName()) {
                    case "uid":
                        userCode = cookies[i].getValue();
                        System.out.println(System.currentTimeMillis() + ":uid:" + userCode);
                        break;
                }
            }
            return userCode;
        } else {
            System.out.println(System.currentTimeMillis() + ":notLogin:");
            HzbuviException exception = new HzbuviException("notLogin");
            exception.setMsg("请重新登录");
            throw exception;
        }
    }

    public static String verifyValidNum(HttpServletRequest request, String newValidNum) throws HzbuviException {
        //response.setHeader("Access-Control-Allow-Origin", Constants.frontManageUrl);
        Cookie[] cookies = request.getCookies();
        String validNumer = "";
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                switch (cookies[i].getName()) {
                    case "validNum":
                        validNumer = cookies[i].getValue();
                        System.out.println(System.currentTimeMillis() + ":validNum:" + validNumer);
                        break;
                }
            }
            if (ObjectUtil.isEmpty(validNumer))
            {
                return "NoValidNum";
            }else {
                if (validNumer.equals(newValidNum)) {
                    return "ValidSucc";
                }else{
                    return "ValidFail";
                }

            }
        } else {
            System.out.println(System.currentTimeMillis() + ":validNum expired:");
            HzbuviException exception = new HzbuviException("validNumExpired");
            exception.setMsg("验证码已过期");
            throw exception;
        }
    }

    public static String verifyValidImg(HttpServletRequest request, String newValidImg) throws HzbuviException {
        //response.setHeader("Access-Control-Allow-Origin", Constants.frontManageUrl);

        String oldImgCode = (String) request.getSession().getAttribute("imgCode");

        if (!ObjectUtil.isEmpty(oldImgCode)) {
            if (oldImgCode.equals(newValidImg)){
                return "ImgValidSucc";
            } else{
                return "ImgValidFail";
            }
        } else {
            System.out.println(System.currentTimeMillis() + ":ImgCode not Found:");
            HzbuviException exception = new HzbuviException("validImgNotFound");
            exception.setMsg("图形验证码未找到");
            throw exception;
        }
    }

    public static void login(HttpServletResponse response, String userCode) {
        //response.setHeader("Access-Control-Allow-Origin", Constants.frontManageUrl);
        Cookie user = new Cookie("uid", userCode);
        user.setPath("/");
        System.out.println(System.currentTimeMillis() + ":login:" + userCode);
        response.addCookie(user);
    }

    public static void validNum(HttpServletResponse response, String validNum) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //response.setHeader("Access-Control-Allow-Origin", Constants.frontManageUrl);
        //加密后的字符串
        String validMD5= MD5.getMD5(validNum);
        Cookie validNumber = new Cookie("validNum", validMD5);
        validNumber.setPath("/");
        validNumber.setMaxAge(60);
        System.out.println(System.currentTimeMillis() + ":validNum:" + validNum);
        response.addCookie(validNumber);
    }

    public static void loginBack(HttpServletResponse response, String userCode) {
        //response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
        Cookie user = new Cookie("bid", userCode);
        user.setPath("/");
        System.out.println(System.currentTimeMillis() + ":login:" + userCode);
        response.addCookie(user);
    }

    public static String verifyBack(HttpServletRequest request, HttpServletResponse response) throws HzbuviException {
     //   response.setHeader("Access-Control-Allow-Origin", Constants.backendManageUrl);
        Cookie[] cookies = request.getCookies();
        String userCode = "";
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                switch (cookies[i].getName()) {
                    case "bid":
                        userCode = cookies[i].getValue();
                        System.out.println(System.currentTimeMillis() + ":bid:" + userCode);
                        break;
                }
            }
            return userCode;
        } else {
            System.out.println(System.currentTimeMillis() + ":notLogin:");
            HzbuviException exception = new HzbuviException("notLogin");
            exception.setMsg("请重新登录");
            throw exception;
        }
    }

   /* private static Map<String,Integer> sessMap = new HashMap<>();

    public static void put(String xSession , Integer  userId ){
        sessMap.put(xSession,userId);
    }

    public static  Integer userId(String xSession){
        return sessMap.get(xSession);
    }

    public static Integer verifyId(HttpServletRequest request) throws HzbuviException {
        Integer id = userId(request.getHeader("X-Session"));
        if (null == id) {
            throw new HzbuviException("notLogin");
        }
        return id;
    }

    public static Integer verifyFrontId(HttpServletRequest request) throws HzbuviException {
        Integer id = userId(request.getHeader("F-Session"));
        if (null == id) {
            throw new HzbuviException("notLogin");
        }
        return id;
    }

    public static void destroy(HttpServletRequest request){
        sessMap.remove(request.getHeader("X-Session"));
    }

    public static void destroyFront(HttpServletRequest request){
        sessMap.remove(request.getHeader("F-Session"));
    }*/
}
