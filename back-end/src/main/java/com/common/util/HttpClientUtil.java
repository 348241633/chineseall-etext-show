package com.common.util;


import com.alibaba.fastjson.JSON;
import com.edusoft.sysmanage.vo.OauthUserInfo;
import org.apache.commons.codec.binary.*;
import org.apache.commons.lang3.*;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.util.DigestUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Created by lego-jspx01 on 2018/2/28.
 */
public class HttpClientUtil {
    public static final String EDCODE_KEY = "ChineseAllShanghai!@#$%^&*()";


    public static String encodePassWord(String pwd) {
        pwd = pwd + EDCODE_KEY;
        String encodeStr=DigestUtils.md5DigestAsHex(pwd.getBytes());
        byte[] result = org.apache.commons.codec.binary.Base64.encodeBase64(encodeStr.getBytes());
        return new String(result);
    }




    public static String postUrl(Map<String,Object> map,String urlstr) {
        String result = null;
        URL url = null;
        HttpURLConnection conn = null;
        StringBuffer sb = new StringBuffer();
        try {
            //创建连接
            url = new URL(urlstr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json; charset=utf-8");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.connect();
            //json参数
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            String input = JSON.toJSONString(map);
            out.write(input.getBytes());
            out.flush();
            out.close();
            //获取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String lines;
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
            reader.close();
            result = sb.toString();
        } catch (Exception e) {
            result = e.getMessage();
        } finally {
            // 关闭连接
            if (null != conn)
                conn.disconnect();
        }
        return result;
    }

    public static String getUrl(String token,String urlstr) {
        CloseableHttpClient httpCilent2 = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)   //设置连接超时时间
                .setConnectionRequestTimeout(5000) // 设置请求超时时间
                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)//默认允许自动重定向
                .build();
        HttpGet httpGet2 = new HttpGet(urlstr);
        if(!org.apache.commons.lang3.StringUtils.isEmpty(token))
            httpGet2.setHeader("token", token);
        httpGet2.setConfig(requestConfig);
        String srtResult = "";
        try {
            HttpResponse httpResponse = httpCilent2.execute(httpGet2);
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                srtResult = EntityUtils.toString(httpResponse.getEntity());//获得返回的结果
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpCilent2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return srtResult;
    }
}
