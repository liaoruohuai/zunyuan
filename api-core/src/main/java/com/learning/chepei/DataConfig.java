
package com.learning.chepei;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * Created by WANG, RUIQING on 10/13/16
 * Twitter : @taylorwang789
 * E-mail : i@wrqzn.com
 */

@Configuration
@Component
public  class DataConfig {


    @Value("${sms.smsname}")
    private String sms_name;

    @Value("${sms.smspwd}")
    private String sms_pwd;
    @Value("${sms.smsurl}")
    private String sms_url;


    public static String SMS_NAME;
    public static String SMS_PWD;
    public static String SMS_URL;


    public DataConfig() {
    }


    @PostConstruct
    public void initProperties(){
        SMS_NAME = sms_name;
        SMS_PWD = sms_pwd;
        SMS_URL = sms_url;
    }
}

