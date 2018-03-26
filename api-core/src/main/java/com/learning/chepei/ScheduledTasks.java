package com.learning.chepei;

import com.learning.chepei.util.WeixinKit;
import com.learning.login.entity.SysConfig;
import com.learning.login.service.LoginService;
import com.learning.util.basic.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger("ScheduledTasks");
    @Autowired
    private LoginService loginService;

    @Scheduled(fixedRate = 1000*5400)
    public void UpdateWxAccessToken(){
        SysConfig wxConfig = loginService.getAccessToken();
        logger.info("上次Token更新时间=[" + wxConfig.getLastTime() + "], Content=[" + wxConfig.getContent() + "]" );
        String result = WeixinKit.getAccessToken();
        loginService.saveAccessToken(result);
        logger.info("本次Token更新成功，等待1.5小时后再次更新" );
    }
}
