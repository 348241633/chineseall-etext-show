package com.edusoft.sysmanage.service;

import com.edusoft.sysmanage.vo.OauthUserInfo;

import java.util.Map;

/**
 * Created by lego-jspx01 on 2018/7/16.
 */
public interface AuthService {

    String getAccessToken(String code);

    Map<String,String> getUserInfo(String accessToken);

    OauthUserInfo getUserByGet(String urlstr,String token);
}
