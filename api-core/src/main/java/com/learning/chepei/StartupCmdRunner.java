package com.learning.chepei;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value=1)
public class StartupCmdRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger("StartupCmdRunner");
    @Override
    public void run(String... args) throws Exception {
        logger.info("---------------启动数据初始化，执行加载数据等操作---------------");
        logger.info("---------------1、加载数据项，微信公众号接口调用Token-----------");
        logger.info("---------------启动数据初始化，执行完毕-------------------------");
    }

    private void getWxAccessToken(){
    }
}
