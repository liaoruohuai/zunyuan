package com.learning.login.service;

import com.learning.login.entity.SmsLog;
import com.learning.login.repository.SmsLogRepository;
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

    public String genValidNum() throws HzbuviException
    {
        String[] beforeShuffle = new String[] { "1","2", "3", "4", "5", "6", "7","8", "9", "0" };
        List list = Arrays.asList(beforeShuffle); //将数组转成List
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        String result = afterShuffle.substring(3, 9);
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
