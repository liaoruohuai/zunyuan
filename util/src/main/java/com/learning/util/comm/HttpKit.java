package com.learning.util.comm;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.learning.util.date.DateUtil;
import org.apache.tomcat.util.codec.binary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpKit {
    private static Logger logger = LoggerFactory.getLogger(HttpKit.class);

    private HttpKit() {
    }

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String CHARSET = "UTF-8";

    private static HttpURLConnection getHttpConnection(String url, String method, Map<String, String> headers) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        URL _url = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setConnectTimeout(19000);
        conn.setReadTimeout(19000);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        if (headers != null && !headers.isEmpty())
            for (Entry<String, String> entry : headers.entrySet())
                conn.setRequestProperty(entry.getKey(), entry.getValue());

        return conn;
    }

    /**
     * 发送 get 请求
     */
    public static String get(String url, Map<String, String> queryParas, Map<String, String> headers) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), GET, headers);
            conn.connect();
            return readResponseString(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static String get(String url, Object queryParas) {
        Map<String, String> map = getQueryParas(queryParas);
        return get(url, map, null);
    }

    public static String get(String url, Map<String, String> queryParas) {
        return get(url, queryParas, null);
    }

    public static String get(String url) {
        return get(url, null, null);
    }

    /**
     * 发送 POST 请求
     * 考虑添加一个参数 Map<String, String> queryParas： getHttpConnection(buildUrl(url, queryParas), POST, headers);
     */
    public static String post(String url, Map<String, String> queryParas, String data, Map<String, String> headers) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), POST, headers);
            conn.connect();

            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes(CHARSET));
            out.flush();
            out.close();

            return readResponseString(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static String post(String url, Map<String, String> queryParas, String data) {
        return post(url, queryParas, data, null);
    }

    public static String post(String url, String data, Map<String, String> headers) {
        return post(url, null, data, headers);
    }

    public static String post(String url, String data) {
        return post(url, null, data, null);
    }

    private static String readResponseString(HttpURLConnection conn) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        try {
            inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, CHARSET));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 在 url 之后构造 queryString
     */
    public static String buildUrlWithQueryString(String url, Map<String, String> queryParas) {
        if (queryParas == null || queryParas.isEmpty()) {
            return url;
        }

        StringBuilder sb = new StringBuilder(url);
        boolean isFirst;
        if (url.indexOf("?") == -1) {
            isFirst = true;
            sb.append("?");
        } else {
            isFirst = false;
        }

        for (Entry<String, String> entry : queryParas.entrySet()) {
            if (isFirst) isFirst = false;
            else sb.append("&");

            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key);
            if (StrKit.notBlank(value)) {
                try {
                    value = URLEncoder.encode(value, CHARSET);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
                sb.append("=").append(value);
            }
        }
        String urlStr = sb.toString();
        logger.debug("Http url:" + urlStr);
        return urlStr;
    }

    public static Map<String, String> getQueryParas(Object queryParas) {
        Map<String, String> map = new HashMap<String, String>();
        Field field[] = queryParas.getClass().getDeclaredFields();
        for (Field f : field) {
            try {
                f.setAccessible(true);
                Object obj = f.get(queryParas);
                if (obj != null) {
                    String valueStr = null;
                    if (obj instanceof String) {
                        valueStr = (String) obj;
                    } else if (obj instanceof Collection<?>) {
                        valueStr = JsonKit.toJSon(obj);
                    } else if (obj instanceof Object[]) {
                        valueStr = JsonKit.toJSon(obj);
                    } else {
                        valueStr = obj.toString();
                    }
                    map.put(f.getName(), valueStr);
                    logger.debug("fieldName:{}. value:{}", f.getName(), valueStr);
                }
            } catch (Exception e) {
                logger.error("fieldName:{}. press error:{}", f.getName(), e.getMessage());
            }
        }
        return map;
    }

    public static String readIncommingRequestData(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            StringBuilder result = new StringBuilder();
            br = request.getReader();
            for (String line = null; (line = br.readLine()) != null; ) {
                result.append(line).append("\n");
            }

            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error("cloase Request reader error.", e);
                }
            }
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        //天畅短信
        String url = "http://101.227.68.68:7891/mt";
        Map<String, String> info = new HashMap<>();
        String timestamp = DateUtil.toString(new Date(),"yyyyMMddHHmmss");
        String smspwd = "wb94mh36";
        String smsun = "020790";
        String message = "短信车而是101002";

        info.put("un",smsun);
        String fnlpw = EncryptionKit.md5EncryptBase64(smspwd,smsun,timestamp,message);


        info.put("pw",fnlpw );
    //    info.put("pw","wb94mh36");
        info.put("da","15901780783");
        info.put("dc","8");
        info.put("tf","3");
        info.put("sm",message);

        info.put("ts",timestamp);
        info.put("sa","790");
        info.put("rf","2");


        System.out.println("start..........");

        String result = HttpKit.get(url, info);
        System.out.println(result);


    }
}





