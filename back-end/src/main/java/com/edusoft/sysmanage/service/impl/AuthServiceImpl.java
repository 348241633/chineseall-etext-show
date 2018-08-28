package com.edusoft.sysmanage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.edusoft.sysmanage.service.AuthService;
import com.edusoft.sysmanage.vo.OauthUserInfo;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.transform.Source;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by lego-jspx01 on 2018/7/16.
 */
@Service
public class AuthServiceImpl implements AuthService {
    private Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    public static RestTemplate getRestTemplate() {// 手动添加
        SimpleClientHttpRequestFactory requestFactory=new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(120000);
        List<HttpMessageConverter<?>> messageConverters = new LinkedList<>();
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new SourceHttpMessageConverter<Source>());
        messageConverters.add(new AllEncompassingFormHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        RestTemplate restTemplate=new RestTemplate(messageConverters);
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    @Override
    public String getAccessToken(String url) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();
        String resp = getRestTemplate().getForObject(uri, String.class);
        logger.error("getAccessToken resp = " + resp);
        if(resp.contains("access_token")){
            Map<String,String> map = getParam(resp);
            String access_token = map.get("access_token");
            return access_token;
        }else{
            return null;
        }
    }

    private Map<String,String> getParam(String string){
        Map<String,String> map = new HashMap();
        String[] kvArray = string.split("&");
        for(int i = 0;i<kvArray.length;i++){
            String[] kv = kvArray[i].split("=");
            map.put(kv[0],kv[1]);
        }
        return map;
    }




    @Override
    public Map<String,String> getUserInfo(String url) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();
        String resp = getRestTemplate().getForObject(uri, String.class);
        logger.error("getUserInfo resp = " + resp);
        JSONObject data = JSONObject.parseObject(resp);
        Map<String,String> result = new HashMap<>();
        result.put("loginId", data.getString("id"));
        JSONArray attributes = data.getJSONArray("attributes");
        List<Map<String,String>> list = (List)attributes;
        Map<String,String> subdata = new HashMap<>();
        for( Map<String,String> map : list ){
            subdata.putAll(map);
        }
        result.put("userId", subdata.get("user.userId"));
        result.put("username",subdata.get("user.userName"));
        result.put("loginName",subdata.get("user.loginName"));
        result.put("userType",subdata.get("user.userType"));
        result.put("distinctIds",subdata.get("user.distinctIds"));
        result.put("distinctNames",subdata.get("user.distinctNames"));
        result.put("imageUrl",subdata.get("user.imageUrl"));
        result.put("token",subdata.get("user.token"));
        result.put("auths",subdata.get("auth.auths"));
        result.put("organId",subdata.get("organ.organId"));
        result.put("organName",subdata.get("organ.organName"));
        result.put("deptId",subdata.get("dept.deptId"));
        result.put("deptName",subdata.get("dept.deptName"));
        return result;
    }

    @Override
    public OauthUserInfo getUserByGet(String urlstr, String token){
        CloseableHttpClient httpCilent2 = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)   //设置连接超时时间
                .setConnectionRequestTimeout(5000) // 设置请求超时时间
                .setSocketTimeout(5000)
                .setRedirectsEnabled(true)//默认允许自动重定向
                .build();
        HttpGet httpGet2 = new HttpGet(urlstr);
        httpGet2.setHeader("token", token);
        httpGet2.setConfig(requestConfig);
        String srtResult = "";
        try {
            HttpResponse httpResponse = httpCilent2.execute(httpGet2);
            if(httpResponse.getStatusLine().getStatusCode() == 200){
                srtResult = EntityUtils.toString(httpResponse.getEntity());//获得返回的结果
                Map<String,Object> resultMap = JSON.parseObject(srtResult, Map.class);
                if(0 == Integer.parseInt(resultMap.get("code").toString())){
                    OauthUserInfo oauthUserInfo =  JSON.parseObject(resultMap.get("data").toString(), OauthUserInfo.class);
                    return oauthUserInfo;
                }
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
        return null;
    }
}
