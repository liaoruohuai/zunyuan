package com.learning.login.service;

import com.learning.login.entity.SmsLog;
import com.learning.login.repository.SmsLogRepository;
import com.learning.util.comm.RanKit;
import com.learning.util.date.DateUtil;
import com.learning.util.exception.HzbuviException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/12/10.
 */
@Service
public class SmsLogService {
    @Autowired
    private SmsLogRepository smslogRepository;

    public String genValidNum()
    {
        String result = RanKit.getRAN(3, 9);
        return result;
    }

    public void saveSmsLog(String phone, String message, String result){
        String timestamp = DateUtil.toString(new Date(),"yyyyMMddHHmmss");
        SmsLog smsLog = new SmsLog();
        smsLog.setPhoneNum(phone);
        smsLog.setSmsContent(message);
        smsLog.setSmsDate(timestamp);
        smsLog.setSmsResult(result);
        insert(smsLog);
    }
    public void insert(SmsLog smslog){
        smslogRepository.save(smslog);
    }

}
