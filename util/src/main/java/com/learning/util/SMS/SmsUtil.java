package com.learning.util.SMS;

import com.learning.util.comm.EncryptionKit;
import com.learning.util.comm.HttpKit;
import com.learning.util.comm.Propkit;
import com.learning.util.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            str = SmsUtil.send("15901780783","测试短信沿着码123123");
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
        Propkit prop = new Propkit();
        prop.loadPropertyFile("db.properties");
        String smsname = prop.getProperty("sms.smsname");
        String smspwd  = prop.getProperty("sms.smspwd");
        String smsurl  = prop.getProperty("sms.smsurl");
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

        System.out.println("start..........");
        String result = HttpKit.get(smsurl, info);
        System.out.println(result);

        return result;

    }





}
