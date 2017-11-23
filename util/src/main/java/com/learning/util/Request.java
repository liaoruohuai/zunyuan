package com.learning.util;

import com.learning.util.basic.MapUtil;
import com.learning.util.basic.ValueUtil;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Post Method
 */
public class Request {

    /**
     * 向指定URL发送POST请求
     * @param url
     * @param paramMap
     * @return 响应结果
     */

    public static String baseUrl = "http://139.196.153.220:8889";

    public static String sendPost(String url,Map<String,Object> head , Map<String,Object> body){
        url = baseUrl + url;
        Map<String,Object> map = new HashMap<>();
        map.put("head",head);
        map.put("body",body);
        String reqMsg = ValueUtil.toJson(map);
        System.out.println(reqMsg);
        return sendPost(url, MapUtil.toMap("reqMessage",reqMsg));
    }


    public static String sendPost(String url, Map<String, Object> paramMap) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // conn.setRequestProperty("Charset", "UTF-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());

            // 设置请求属性
            String param = "";
            if (paramMap != null && paramMap.size() > 0) {
                Iterator<String> ite = paramMap.keySet().iterator();
                while (ite.hasNext()) {
                    String key = ite.next();// key
                    String value = (String)paramMap.get(key);
                    param += key + "=" + value + "&";
                }
                param = param.substring(0, param.length() - 1);
            }

            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.err.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 数据流post请求
     * @param urlStr
     * @param
     */
    public static String doPost(String urlStr, String strInfo) {
        urlStr = baseUrl + urlStr;
        String reStr="";
        try {
            URL url = new URL(urlStr);
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("Pragma:", "no-cache");
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("Content-Type", "text/xml");
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
            out.write(new String(strInfo.getBytes("utf-8")));
            out.flush();
            out.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
            String line = "";
            for (line = br.readLine(); line != null; line = br.readLine()) {
                reStr += line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reStr;
    }

    public static void main(String[] args) {
//        Map<String, Object> mapParam = new HashMap<>();
//        mapParam.put("reqMessage", "{     \"head\": {         \"APP_INFO\": \"中文\",         \"BNKNBR\": \"000342\",         \"BRN_NO\": \"test111\",         \"OPE_NO\": \"11111\",         \"SEQNO\": \"1234523456\",         \"SOURCE\": \"33\",         \"TRXTYPE\": \"1001\"     },     \"body\": {         \"AGE\": \"23\",         \"APPLY_DATE\": \"20160908\",         \"APPLY_LIMIT\": \"100000\",         \"APPLY_PERIOD\": \"1\",         \"APPROVE_DAILY_LIMIT\": \"10000\",         \"APPROVE_LIMIT\": \"100000\",         \"APPROVE_MONTH_LIMIT\": \"50000\",         \"APP_USR\": \"C\",         \"BILL_DATE\": \"10\",         \"BIRTH\": \"\",         \"CENSUS_ADD1\": \"\",         \"CENSUS_ADD2\": \"\",         \"CENSUS_ADD3\": \"\",         \"CENSUS_ADD4\": \"\",         \"CENSUS_POSTCODE\": \"\",         \"CHILD_NUM\": \"0\",         \"COMPANY_ADD1\": \"\",         \"COMPANY_ADD2\": \"\",         \"COMPANY_ADD3\": \"\",         \"COMPANY_ADD4\": \"\",         \"COMPANY_NAME\": \"\",         \"COMPANY_PHONE\": \"\",         \"COMPANY_POSITION\": \"\",         \"COMPANY_POSTCODE\": \"\",         \"COMPANY_SIZE\": \"\",         \"COMPANY_TYPE\": \"\",         \"COP_ACCT_ID\": \"12456\",         \"COP_ADDR\": \"公司地址\",         \"COP_CAR_CARD\": \"公司卡编号\",         \"COP_CAR_NUM\": \"\",         \"COP_ID\": \"1234567890\",         \"COP_LAST_PROFIT\": \"\",         \"COP_NAME\": \"申请单位名称\",         \"COP_ORG_ID\": \"\",         \"COP_OWNER_ID\": \"\",         \"COP_OWNER_NAME\": \"法人姓名\",         \"COP_OWNER_SPOUS\": \"\",         \"COP_PLACE\": \"\",         \"COP_SETUP_DATE\": \"\",         \"COP_STAFF_NUM\": \"\",         \"COP_TAX_ID\": \"\",         \"COP_TEL\": \"\",         \"COP_TRADE\": \"\",         \"COP_TYPE\": \"\",         \"CREATE_USR\": \"\",         \"CREDIT_ACCT\": \"\",         \"DEGREE\": \"\",         \"EMAIL\": \"\",         \"ESTABLISH_DATE\": \"\",         \"FINAL_DATE\": \"\",         \"HOME_ADD1\": \"\",         \"HOME_ADD2\": \"\",         \"HOME_ADD3\": \"\",         \"HOME_ADD4\": \"\",         \"HOME_PHONE\": \"\",         \"HOME_POSTCODE\": \"\",         \"HOME_STATUS\": \"\",         \"HOUSE_DATE\": \"\",         \"HOUSE_PRICE\": \"\",         \"HOUSE_REGION\": \"\",         \"HOUSE_SIZE\": \"\",         \"ID\": \"\",         \"ID_TYPE\": \"\",         \"IFCHILD\": \"\",         \"IMFAMILY_NAME\": \"\",         \"IMFAMILY_PHONE\": \"\",         \"IMFAMILY_RELATIONSHIP\": \"\",         \"LINKEDMAN_NAME\": \"\",         \"LINKEDMAN_PHONE\": \"\",         \"LINKEDMAN_RELATIONSHIP\": \"\",         \"LOAN_DESC\": \"\",         \"LOAN_MONEY\": \"10000\",         \"MARITALSTATUS\": \"\",         \"MOBILE\": \"\",         \"MONTHLY_INCOME\": \"10000\",         \"MONTHLY_INCOME_DAY\": \"\",         \"NAME\": \"\",         \"OTHER_INCOME\": \"0\",         \"OTHER_INCOME_SOURCE\": \"\",         \"OWN_CAR\": \"\",         \"PERIOD\": \"1\",         \"PRDCT_NO\": \"9999\",         \"PROMOT_NAME\": \"\",         \"PROMOT_NUM\": \"\",         \"PROMOT_ORG_NAME\": \"\",         \"PROMOT_ORG_NUM\": \"\",         \"REGISTER_MONEY\": \"\",         \"REPAY_TYPE\": \"\",         \"SECTION\": \"\",         \"SEX\": \"\",         \"SPOUSE\": \"\",         \"SPOUSE_NAME\": \"\",         \"SPOUSE_PHONE\": \"\",         \"START_POST_YEAR\": \"\",         \"STOCK_PROP\": \"\",         \"TABLET_DATE\": \"20160908\",         \"UPDATE_USR\": \"\",         \"WITHHOLE_ACCT_BANK\": \"aaaaaa\",         \"WITHHOLE_ACCT_CARD\": \"\",         \"WITHHOLE_ACCT_NAME\": \"\",         \"YAER_IN_COMPANY\": \"0\"     } }");
//        String pathUrl = "http://139.196.153.220:8889/cfc/oappSync.do";
//        String result = sendPost(pathUrl, mapParam);
//        System.out.println(result);
//
        String result = sendPost("/cfc/oLimitMod.do"
                , MapUtil.toMap("TRXTYPE","2004","BNKNBR","000342","SOURCE",1001)
                , MapUtil.toMap("CUST_ID","6000000182","ID_TYPE","G") );

        System.out.println(result);
    }

}