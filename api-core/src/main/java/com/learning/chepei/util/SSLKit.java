package com.learning.chepei.util;

import com.learning.chepei.DataConfig;
import com.learning.chepei.controller.TransmitService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class SSLKit {

    public static JaxWsProxyFactoryBean svr;

    public static TransmitService transmitService;

    public static String sslSend(String message) {
        Map<String, String> info = new HashMap<>();
        System.setProperty("javax.net.ssl.trustStore",DataConfig.TRUST_STORE); // 证书路径
        System.setProperty("javax.net.ssl.trustStorePassword", DataConfig.SSL_PASSWD);
        transmitService = null;
        svr = new JaxWsProxyFactoryBean();
        svr.setServiceClass(TransmitService.class);
        svr.setAddress(DataConfig.SSL_ADDRESS);
        transmitService = (TransmitService) svr.create();
        String result = transmitService.sendService(message);
        return result;
    }
/*
    public static String socketSend(String acceptContentFormOut) {
        String sendXmlToSd = acceptContentFormOut.toString();
        String sendXmlByteLengthToSd;
        SocketConnect s = null;
        try {
            sendXmlByteLengthToSd = CommonFunction.fillString(
                    sendXmlToSd.getBytes(Constants.XMLENCODING).length + "",
                    '0', 4, false);

            String sendContentToSd = sendXmlByteLengthToSd + sendXmlToSd;
            System.out.println("长度:" + sendXmlByteLengthToSd + ",发送的报文:"
                    + sendContentToSd);

            // socket通信
            // String ip = Config.getCoreIp() ;
            // String port=Config.getCorePort();
            String ip = "10.250.3.28";
            String port = "5679";
            s = new SocketConnect(sendContentToSd, port, ip);

            s.run("GBK");

            // 接收收单应答的报文
            String acceptXmlFormSd = s.getRsp().trim();
            String acceptXmlLengthFormSd = acceptXmlFormSd.substring(0, 4);
            String acceptContentFormSd = acceptXmlFormSd.substring(4);
            System.out.println("长度:" + acceptXmlLengthFormSd + ",接收到响应的报文:"
                    + acceptContentFormSd);
            s.close();
            // 如果收单返回的报文中包含有XML报文头,需要将XML报文头剔除
            if (acceptContentFormSd.indexOf(Constants.XMLBAOWENHEADER) == 0) {
                acceptContentFormSd = acceptContentFormSd
                        .substring(Constants.XMLBAOWENHEADER.length());
                System.out.println("发送给外部的报文:" + acceptContentFormSd);
            }
            return acceptContentFormSd;
        } catch (Exception e) {
            System.out.println("webservice解析,组装,通信异常:" + e.getMessage());
            if (s != null) {
                try {
                    s.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
            return Constants.SYSERR;
        }

    }
    */
}
