package com.learning.order.service;

import com.learning.util.date.DateUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.DefaultHttpParams;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author:GR
 * Date:2016/11/11
 * Discription:
 */
public class HttpClientUtil {
    private static String appKey = "cxj_coupon_service_api";   //应用key
    private static String securityKey = "93f4Gho8";  //秘钥
    private static String timestamp; //时间戳，半小时时间差许可

    public static String setHttpHead(HttpClient httpClient,String httpUrl,String service,String method,String message){
        DefaultHttpParams.getDefaultParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
        Map<String, String> headParams = new HashMap<>();
        timestamp= DateUtil.toString(new Date(),"yyyyMMddHHmmss");
        headParams.put("appKey", appKey);
        headParams.put("timestamp", timestamp);
        headParams.put("format", "json");
        headParams.put("signatureMethod", "md5");
        headParams.put("version", "1");
        headParams.put("signature", make(headParams, service, method, message, securityKey));
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(3000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(3000);
        PostMethod postMethod = new PostMethod(httpUrl);
        Set<String> keySet = headParams.keySet();
        for (String key : keySet) {
            postMethod.setRequestHeader(key, headParams.get(key));
        }
        postMethod.setRequestBody(message);
        postMethod.setRequestHeader("Content-Type", "text/html;charset=UTF-8");
        try {
            int httpStatus = httpClient.executeMethod(postMethod);
            System.out.println("httpStatus: " + httpStatus);
            byte[] result = postMethod.getResponseBody();
            System.out.println("result: " + new String(result));
            return new String(result);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String make(Map<String, String> headParams,String service,String method, String message,
                              String securityKey) {
        String signature = md5(makeOriginalString(headParams,service,method,message,securityKey));
        System.out.println("signature : " + signature);
        return signature;

    }

    private static String makeOriginalString(Map<String, String> headParams, String service,String method,String message,
                                            String securityKey){
        StringBuilder builder = new StringBuilder();
        builder.append(service);
        builder.append(method);
        builder.append(message);
        builder.append("appKey").append("=").append(headParams.get("appKey"));
        builder.append("format").append("=").append(headParams.get("format"));
        builder.append("timestamp").append("=").append(headParams.get("timestamp"));
        builder.append("signatureMethod").append("=").append(headParams.get("signatureMethod"));
        builder.append("version").append("=").append(headParams.get("version"));
        builder.append(securityKey);
        String contents = builder.toString();
        System.out.println("contents : " + contents);
        return  contents;
    }

    private final static String md5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
