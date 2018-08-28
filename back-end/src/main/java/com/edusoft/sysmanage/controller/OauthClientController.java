package com.edusoft.sysmanage.controller;

import com.edusoft.sysmanage.service.AuthService;
import com.edusoft.sysmanage.service.UserInfoService;
import com.edusoft.sysmanage.vo.OauthUserInfo;
import com.edusoft.sysmanage.vo.UserInfoVo;
import com.common.security.JwtUtil;
import com.common.util.resultjson.EntityJsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lego-jspx01 on 2018/7/16.
 */
@Api(value = "接入cas服务器", description = "通过oauth2接入cas服务器",tags = "oauthclient", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping("/user")
public class OauthClientController {
    private static final Log log = LogFactory.getLog(OauthClientController.class);
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    AuthService authService;

    @Value("${cas.oauth2.server.url}")
    private String casUrl;
    @Value("${api.url}")
    private String apiUrl;
    @Value("${cas.oauth2.client}")
    private String clentId;
    @Value("${cas.oauth2.secret}")
    private String secret;

    private static final String authorizeUrl = "/oauth2/authorize";
    private static final String accessTokenUrl = "/oauth2/accessToken";
    private static final String profileUrl = "/oauth2/profile?access_token=%s";
    private static final String getUserInfo = "/api/2/get_user_info?userId=%s&clientId=%s";


    @ApiOperation(value = "校验url携带的user_id，access_token")
    @ResponseBody
    @GetMapping(value = "/casInfo")
    public EntityJsonResult casInfo(Long userId,String accessToken )throws Exception {
        EntityJsonResult entityJsonResult = new EntityJsonResult("success","");
        String url = String.format(apiUrl+getUserInfo, userId,clentId);
        OauthUserInfo oauthUserInfo = authService.getUserByGet(url, accessToken);
        if(null == oauthUserInfo){
            entityJsonResult = new EntityJsonResult("fail","token fail");
            Map<String,String> result = new HashMap<>();
            result.put("auth_url",casUrl + authorizeUrl);
            result.put("token_url",casUrl + accessTokenUrl);
            result.put("client_id",clentId);
            result.put("client_secret",secret);
            entityJsonResult.setData(result);
        }else{//oauth用户是否已存储在本地，有返回本地用户信息，没有插入本地库并返回
            UserInfoVo userInfoVo = userInfoService.selectOrInsertLoginUser(oauthUserInfo);
            userInfoVo.setToken(JwtUtil.createToken(userInfoVo.getId(), userInfoVo.getUsername()));
            entityJsonResult.setData(userInfoVo);
        }
        return entityJsonResult;
    }


    @ApiOperation(value = "通过access_token获取信息")
    @ResponseBody
    @GetMapping(value = "/profile")
    public EntityJsonResult profile(String accessToken, HttpServletRequest request,HttpServletResponse response) throws Exception{
        EntityJsonResult entityJsonResult = new EntityJsonResult("success","");
        String url = String.format(casUrl + profileUrl, accessToken);
        Map<String,String> result = authService.getUserInfo(url);
        System.out.println(result.toString());
        entityJsonResult.setData(result);
        return entityJsonResult;
    }


    @ApiOperation(value = "通过code获取信息")
    @ResponseBody
    @GetMapping(value = "/codeProfile")
    public EntityJsonResult codeprofile(String code,String redirect_uri, HttpServletRequest request,HttpServletResponse response) throws Exception{
        EntityJsonResult entityJsonResult = new EntityJsonResult("success","");
        String url = casUrl + accessTokenUrl +"?client_id="+clentId+"&redirect_uri="+redirect_uri+"&client_secret="+secret+"&code="+code;
        String accessToken = authService.getAccessToken(url);
        url = String.format(casUrl + profileUrl, accessToken);
        Map<String,String> result = authService.getUserInfo(url);
        System.out.println(result.toString());
        entityJsonResult.setData(result);
        return entityJsonResult;
    }
}
