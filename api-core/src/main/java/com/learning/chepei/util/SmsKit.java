package com.learning.chepei.util;

import com.learning.chepei.DataConfig;
import com.learning.login.entity.SmsLog;
import com.learning.login.service.SmsLogService;
import com.learning.util.comm.EncryptionKit;
import com.learning.util.comm.HttpKit;
import com.learning.util.comm.Propkit;
import com.learning.util.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SmsKit {

    private static final Logger logger = LoggerFactory.getLogger("SmsKit");

    @Autowired
    private SmsLogService smsLogService;
    /**
     * @param phone 手机号码
     * @param message    内容
     * @return
     */
    public static String send(String phone,  String message) throws UnsupportedEncodingException {
        Map<String, String> info = new HashMap<>();
        String timestamp = DateUtil.toString(new Date(),"yyyyMMddHHmmss");

        String smsname = DataConfig.SMS_NAME;
        String smspwd  = DataConfig.SMS_PWD;
        String smsurl  = DataConfig.SMS_URL;
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
        System.out.println("*************");
        System.out.println("start...send..sms....."+phone);
        String result = HttpKit.get(smsurl, info);
        System.out.println(result);

        return result;

    }





}
