package com.learning.util.basic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * ClassName: HttpURLService
 */
public class HttpURLService {

	public static String sendHttp(String send_url){
		URL url = null;
		HttpURLConnection httpurlconnection = null;
		try {
			url = new URL(send_url);

			// 以post方式请求
			httpurlconnection = (HttpURLConnection) url.openConnection();
			httpurlconnection.setDoOutput(true);
			httpurlconnection.setRequestMethod("GET");
			httpurlconnection.getOutputStream().flush();
			httpurlconnection.getOutputStream().close();
			
			// 获取响应代码
			int code = httpurlconnection.getResponseCode();
			System.out.println("code   " + code);

			// 获取页面内容
			java.io.InputStream in = httpurlconnection.getInputStream();
			BufferedReader breader = new BufferedReader(
					new InputStreamReader(in, "UTF-8"));
			String str = breader.readLine();

			String status = "0000";

			while (str != null) {
				System.out.println(str);
				JsonParser parser = new JsonParser();
				JsonElement json = parser.parse(str);
				status = json.getAsJsonObject().get("status").toString()
						.replace("\"", "");
				System.out.println("status:" + status);
				str = breader.readLine();
			}

			return status;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (httpurlconnection != null)
				httpurlconnection.disconnect();
		}
	}
}
