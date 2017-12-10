package com.learning.login.service;

import com.learning.login.entity.SmsLog;
import com.learning.login.repository.SalerRepository;
import com.learning.login.repository.SmsLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/12/10.
 */
@Service
public class SmsLogService {
    @Autowired
    private SmsLogRepository smslogRepository;

    public void insert(SmsLog smslog){
        smslogRepository.save(smslog);
    }

}
