package com.learning.util.SMS;

import com.learning.util.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Date;

/**
 * Created by zhonghua on 2017/6/20.
 */
public class SmsUtil {

    private static final Logger logger = LoggerFactory.getLogger("SmsUtil");

    public static void main(String[] args) {
        String str = send("15021162660", "10051", "DJ14e1a3641c", "测试商品-洗车-20次");// 返回RESP_FLAG =00代表成功
        System.out.println(str);
    }

    /**
     * @param phone 手机号码
     * @param no    模版号
     * @param cv    参数 REQUEST_TIME为日期
     * @return
     */
    public static String send(String phone, String no, String cv, String productName) {
        String nowDate = DateUtil.toString(new Date(), "yyyyMMddHHmmss");
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><DATA><SMS>" + no
                + "</SMS><SMS_TYPE>0</SMS_TYPE><MOBILE>" + phone + "</MOBILE><MIS1>" + productName + "</MIS1><CV>" + cv
                + "</CV><REQUEST_TIME>" + nowDate + "</REQUEST_TIME></DATA>";

        logger.info("短信报文：" + xml.length() + "----------" + xml);

        int length = 0;
        try {
            length = xml.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return socket(codeAddOne(length + "", 4) + xml);
    }

    @SuppressWarnings("resource")
    public static String socket(String clearText) {
        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;
        try {
//			socket = new Socket("10.250.5.55", 29081);
            socket = new Socket("10.100.84.98", 9080);
            socket.setKeepAlive(true);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            byte[] b = new byte[1024];
            out.write(clearText.getBytes());
            int count = 0;
            while (count == 0) {
                count = in.available();
            }
            byte[] b2 = new byte[2];
            in.read(b2);
            byte[] b1 = new byte[count];
            in.read(b1);
            return new String(b1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String codeAddOne(String code, int len) {
        Integer intHao = Integer.parseInt(code);
        String strHao = intHao.toString();
        while (strHao.length() < len) {
            strHao = "0" + strHao;
        }
        return strHao;
    }
}
