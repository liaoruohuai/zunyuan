package com.learning.util.SMS;

//import com.learning.chepei.DataConfig;
import com.learning.util.comm.EncryptionKit;
import com.learning.util.comm.HttpKit;
import com.learning.util.comm.Propkit;
import com.learning.util.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhonghua on 2017/6/20.
 */

@Configuration
public class SmsUtil {

    private static final Logger logger = LoggerFactory.getLogger("SmsUtil");



    public static void main(String[] args) {
        String str = null;
        try {
            //str = SmsUtil.send("13611819694","测试短信沿着码123123");
            str = SmsUtil.send("13611819694","验证码: 123123（请提供给发卡员完成确认），提供该验证码即视为同意提供您的相关信息以供办理中石油上汽联名卡，如非本人操作请忽略此短信");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(str);
    }

    /**
     * @param phone 手机号码
     * @param message    内容
     * @return
     */
    public static String send(String phone,  String message) throws UnsupportedEncodingException {
        Map<String, String> info = new HashMap<>();
        String timestamp = DateUtil.toString(new Date(),"yyyyMMddHHmmss");

        //String smsname =  "020790";// DataConfig.SMS_NAME;
        //String smspwd  = "wb94mh36";//DataConfig.SMS_PWD;
        String smsname =  "020984";// DataConfig.SMS_NAME;
        String smspwd  = "sk59me34";//DataConfig.SMS_PWD;
        String smsurl  = "http://101.227.68.68:7891/mt";//DataConfig.SMS_URL;
        String fnlpw = EncryptionKit.md5EncryptBase64(smspwd,smsname,timestamp,message);

        info.put("pw",fnlpw);
        info.put("un",smsname);
        info.put("da",phone);
        info.put("dc","8");
        info.put("tf","3");
        info.put("sm", message);
        info.put("ts",timestamp);
        info.put("sa","790");
        info.put("rf","2");

        System.out.println("start...send..sms....."+phone);
        String result = HttpKit.get(smsurl, info);
        System.out.println(result);

        return result;

    }





}
