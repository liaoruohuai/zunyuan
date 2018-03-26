package com.learning.chepei.controller;

import com.learning.chepei.util.WeixinKit;
import com.learning.util.basic.ValueUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:GR
 * Date:2016/11/9
 * Discription:
 */
@RestController
@RequestMapping("/wx")
public class WeixinController {
    @RequestMapping(value = "/valid", method = RequestMethod.GET)
    public void weixinValid(@RequestParam(value = "signature") String signature,
                            @RequestParam(value = "timestamp") String timestamp,
                            @RequestParam(value = "nonce") String nonce,
                            @RequestParam(value = "echostr") String echostr,
                            HttpServletResponse response){
        System.out.println("Success");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if(WeixinKit.checkSignature(signature, timestamp, nonce)){
                out.write(echostr);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            out.close();
        }
    }

    @RequestMapping("/message")
    public String weixinMessage(HttpServletRequest request, HttpServletResponse response){
        try {

            // 将解析结果存储在HashMap中
            Map<String,String> map = new HashMap<String, String>();

            // 从request中取得输入流
            InputStream inputStream = request.getInputStream();
            // 读取输入流
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点
            List<Element> elementList = root.elements();
            // 遍历所有子节点
            for (Element e : elementList)
                map.put(e.getName(), e.getText());

            // 释放资源
            inputStream.close();
            inputStream = null;


            System.out.println(map.toString());
            return ValueUtil.toJson("status","success","message",map.toString());
        }catch (Exception e) {
            return ValueUtil.toError(e.getMessage(),"");
        }
    }
}
