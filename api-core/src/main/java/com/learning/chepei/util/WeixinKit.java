package com.learning.chepei.util;

import com.learning.chepei.DataConfig;
import com.learning.util.comm.HttpKit;
import com.learning.util.encryption.SHA1;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

@Configuration
public class WeixinKit {

    private static final Logger logger = LoggerFactory.getLogger("WeixinKit");

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?"
            + "grant_type=client_credential&appid=APPID"
            + "&secret=APPSECRET" ;

    private static final String USER_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?"
            + "appid=APPID&secret=APPSECRET"
            + "&code=CODE&grant_type=authorization_code";

    private static final String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?"
            + "access_token=ACCESS_TOKEN"
            + "&openid=OPENID";

    public static boolean checkSignature(String signature,String timestamp,String nonce){
        String[] str = new String[]{DataConfig.WEIXIN_TOKEN,timestamp,nonce};
        //排序
        Arrays.sort(str);
        //拼接字符串
        StringBuffer buffer = new StringBuffer();
        for(int i =0 ;i<str.length;i++){
            buffer.append(str[i]);
        }
        //进行sha1加密
        String temp = SHA1.encode(buffer.toString());
        //与微信提供的signature进行匹对
        return signature.equals(temp);
    }

    public static String getAccessToken(){
        String url = ACCESS_TOKEN_URL.replace("APPID",DataConfig.WEIXIN_APPID).replace("APPSECRET", DataConfig.WEIXIN_APPSECRET);
        String result = HttpKit.get(url);
        JSONObject json = new JSONObject(result);
        Iterator<?> iterator = json.keys();
        String Key="";
        String Value="";
        while (iterator.hasNext()) {
            Key = (String)iterator.next();
            Value = json.getString(Key);
            if ("access_token".equals(Key)) {
                return Value;
            }
        }
        return "Failed";
    }

    public static String getUserAccessToken(String code){
        String url = USER_ACCESS_TOKEN_URL.replace("APPID",DataConfig.WEIXIN_APPID).replace("APPSECRET", DataConfig.WEIXIN_APPSECRET).replace("CODE",code);
        String result = HttpKit.get(url);
        JSONObject json = new JSONObject(result);
        Iterator<?> iterator = json.keys();
        String Key="";
        String Value="";
        while (iterator.hasNext()) {
            Key = (String)iterator.next();
            Value = json.getString(Key);
            if ("openid".equals(Key)) {
                return Value;
            }
        }
        return "Failed";
    }

    public static String getUserInfo(String access_token, String openid){
        String url = USER_INFO_URL.replace("ACCESS_TOKEN",access_token).replace("OPENID",openid);
        logger.info("--------本次获取用户信息url：{ " + url + "}");
        String result = HttpKit.get(url);
        logger.info("--------本次获取用户信息结果：{ " + result + "}");
        JSONObject json = new JSONObject(result);
        Iterator<?> iterator = json.keys();
        String Key="";
        String Value="";
        while (iterator.hasNext()) {
            Key = (String)iterator.next();
            //logger.info("------Key=[" + Key + "]-------");
            if ("headimgurl".equals(Key)) {
                Value = json.getString(Key);
                //logger.info("------Value=[" + Value + "]-------");
                return Value;
            }
        }
        return "Failed";
    }

}
