
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
public   class DataConfig {
    @Value("${sms.smsname}")
    private String sms_name;

    @Value("${sms.smspwd}")
    private String sms_pwd;
    @Value("${sms.smsurl}")
    private String sms_url;

    @Value("${clt.trustStore}")
    private String trustStore;
    @Value("${clt.sslPasswd}")
    private String sslPasswd;

    @Value("${clt.sslAddress}")
    private String sslAddress;

    @Value("${issueCard.channel}")
    private String issueCard_channel;
    @Value("${issueCard.issuer_id}")
    private String issueCard_issuer_id;
    @Value("${issueCard.term_id}")
    private String issueCard_term_id;
    @Value("${issueCard.branch_id}")
    private String issueCard_branch_id;
    @Value("${issueCard.mchnt_cd}")
    private String issueCard_mchnt_cd;

    public static String SMS_NAME;
    public static String SMS_PWD;
    public static String SMS_URL;

    public static String TRUST_STORE;
    public static String SSL_PASSWD;
    public static String SSL_ADDRESS;

    public static String ISSUECARD_CHANNEL;
    public static String ISSUECARD_ISSUER_ID;
    public static String ISSUECARD_TERM_ID;
    public static String ISSUECARD_BRANCH_ID;
    public static String ISSUECARD_MCHNT_CD;

    public DataConfig() {
    }


    @PostConstruct
    public void initProperties(){
        SMS_NAME = sms_name;
        SMS_PWD = sms_pwd;
        SMS_URL = sms_url;
        TRUST_STORE = trustStore;
        SSL_PASSWD = sslPasswd;
        SSL_ADDRESS = sslAddress;
        ISSUECARD_CHANNEL = issueCard_channel;
        ISSUECARD_ISSUER_ID = issueCard_issuer_id;
        ISSUECARD_TERM_ID = issueCard_term_id;
        ISSUECARD_BRANCH_ID = issueCard_branch_id;
        ISSUECARD_MCHNT_CD = issueCard_mchnt_cd;
    }
}

