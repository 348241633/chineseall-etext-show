package com.edusoft.sysmanage.controller;

import com.alibaba.fastjson.JSON;
import com.common.util.HttpClientUtil;
import com.edusoft.sysmanage.model.UserInfo;
import com.edusoft.sysmanage.model.UserInfoExample;
import com.edusoft.sysmanage.model.UserInfoKey;
import com.edusoft.sysmanage.service.UserInfoService;
import com.edusoft.sysmanage.vo.OauthUserInfo;
import com.edusoft.sysmanage.vo.UserInfoVo;
import com.common.security.JwtUtil;
import com.common.security.TokenUtil;
import com.common.util.Base64Util;
import com.common.util.Constants;
import com.common.util.DateUtils;
import com.common.util.resultjson.EntityJsonResult;
import com.common.util.resultjson.JsonResult;
import com.common.util.resultjson.ListJsonResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lego-jspx01 on 2018/6/9.
 */
@Api(value = "用户管理", description = "用户管理API",tags = "UserApi", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Log log = LogFactory.getLog(UserController.class);
    @Autowired
    UserInfoService userInfoService;
    @Value("${msg.url}")
    private String msgUrl;
    @Value("${msg.app.code}")
    private String msgCode;
    @Value("${msg.funcid}")
    private String msgFuncid;
    @Value("${msg.text}")
    private String msgText;

    @ApiOperation(value = "用戶列表")
    @ResponseBody
    @GetMapping(value = "/list")
    public ListJsonResult userList(@RequestParam(value="offset", defaultValue = "1") Integer offset,
                                   @RequestParam(defaultValue = "10") Integer limit,
                                   @RequestParam(required = false) String telNum,
                                   @RequestParam(required = false) String realName) {
        Map<String,Object> map = new HashMap<>();
        map.put("offset", offset);
        map.put("limit", limit);
        UserInfoExample example = new UserInfoExample();
        UserInfoExample.Criteria criteria = example.createCriteria();
        criteria.andCasTypeEqualTo("notCas");//自注册用户
        if(!StringUtils.isEmpty(telNum))
            criteria.andTelNumEqualTo(telNum);
        if(!StringUtils.isEmpty(realName))
            criteria.andUsernameLike(realName);
        List<UserInfo> list = userInfoService.selectByExampleWithRowbounds(example,map);
        PageInfo<UserInfo> page = new PageInfo<UserInfo>(list);
        ListJsonResult json = new ListJsonResult("success","");
        json.setData(page.getList());
        json.setTotal(page.getTotal());
        return json;
    }

    @ApiOperation(value = "新增用户")
    @ResponseBody
    @PostMapping(value = "/add")
    public JsonResult addUser(@RequestBody UserInfo userInfo) {
        JsonResult jsonResult = new JsonResult("success","");
        try{
            UserInfoExample example = new UserInfoExample();
            example.createCriteria().andUsernameEqualTo(userInfo.getUsername());
            List<UserInfo> list = userInfoService.selectByExample(example);
            if(null != list && list.size()>0){
                jsonResult.setStatus("fail");
                jsonResult.setMessage("新增用户失败，用户名重复");
            }else{
                userInfo.setAddUser(TokenUtil.getUserName());
                userInfo.setAddTime(DateUtils.getNowDate());
                userInfo.setDeleted(Constants.DELETE_DEFAULT);
                userInfo.setPwd(Base64Util.EncoderByMd5("111111"));
                userInfoService.insert(userInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setStatus("fail");
            jsonResult.setMessage(e.getMessage());
        }
        return jsonResult;
    }

    @ApiOperation(value = "用户详情")
    @ResponseBody
    @GetMapping(value = "/detail/{id}")
    public EntityJsonResult userDetail(@PathVariable Integer id) {
        EntityJsonResult entityJsonResult = new EntityJsonResult("success","");
        try{
            UserInfoKey key = new UserInfoKey();
            key.setId(id);
            UserInfo userInfo = userInfoService.selectByPrimaryKey(key);
            entityJsonResult.setData(userInfo);
        }catch (Exception e){
            e.printStackTrace();
            entityJsonResult.setStatus("fail");
            entityJsonResult.setMessage(e.getMessage());
        }
        return entityJsonResult;
    }

    @ApiOperation(value = "修改用户")
    @ResponseBody
    @PutMapping(value = "/update/{id}")
    public JsonResult updateUser(@PathVariable Integer id,@RequestBody UserInfo userInfo) {
        JsonResult jsonResult = new JsonResult("success","");
        try{
            userInfo.setId(id);
            userInfo.setUpdateUser(TokenUtil.getUserName());
            userInfo.setUpdateTime(DateUtils.getNowDate());
            UserInfoExample example = new UserInfoExample();
            example.createCriteria().andIdEqualTo(id);
            userInfoService.updateByExampleSelective(userInfo, example);
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setStatus("fail");
            jsonResult.setMessage(e.getMessage());
        }
        return jsonResult;
    }

    @ApiOperation(value = "逻辑删除用户")
    @ResponseBody
    @DeleteMapping(value = "/delete/{id}")
    public JsonResult deleteUser(@PathVariable Integer id) {
        JsonResult jsonResult = new JsonResult("success","");
        try{
            String username = TokenUtil.getUserName();
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setDeleted(Constants.IS_DELETED);
            userInfo.setUpdateTime(DateUtils.getNowDate());
            userInfo.setUpdateUser(username);
            userInfoService.updateByPrimaryKeySelective(userInfo);
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setStatus("fail");
            jsonResult.setMessage(e.getMessage());
        }
        return jsonResult;
    }

    @ApiOperation(value = "发送验证码")
    @ResponseBody
    @PostMapping(value = "/sendCode")
    public EntityJsonResult sendCode(@RequestParam String telNum) {
        EntityJsonResult jsonResult = new EntityJsonResult("fail","发送验证码失败");
        try{
            String code = Base64Util.getCode();
            Map<String,Object> map = new HashMap<>();
            map.put("appcode", msgCode);
            map.put("funcid", msgFuncid);
            map.put("mobile", telNum);
            map.put("text", String.format(msgText, code));
            String result = HttpClientUtil.postUrl(map, msgUrl);
            if(null != result){
                Map<String,Object> resultMap = JSON.parseObject(result, Map.class);
                if(0 == Integer.parseInt(resultMap.get("code").toString())){
                    jsonResult.setData(code);
                    jsonResult.setStatus("success");
                    jsonResult.setMessage("");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setStatus("fail");
            jsonResult.setMessage(e.getMessage());
        }
        return jsonResult;
    }


    @ApiOperation(value = "注册用户")
    @ResponseBody
    @PostMapping(value = "/register")
    public EntityJsonResult registerUser(@RequestBody UserInfo userInfo) {
        EntityJsonResult jsonResult = new EntityJsonResult("success","注册成功");
        try{
            UserInfoExample example = new UserInfoExample();
            example.createCriteria().andTelNumEqualTo(userInfo.getTelNum());
            List<UserInfo> list = userInfoService.selectByExample(example);
            if(null != list && list.size()>0){
                jsonResult.setStatus("fail");
                jsonResult.setMessage("注册失败，用户名重复");
            }else{
                userInfo.setAddTime(DateUtils.getNowDate());
                userInfo.setPwd(Base64Util.EncoderByMd5(userInfo.getPwd()));
                userInfo.setDeleted(Constants.DELETE_DEFAULT);
                userInfo.setCasType("notCas");
                userInfoService.insert(userInfo);
                UserInfoVo userInfoVo = new UserInfoVo();
                userInfoVo.setToken(JwtUtil.createToken(userInfo.getId(),userInfo.getUsername()));
                BeanUtils.copyProperties(userInfo,userInfoVo );
                jsonResult.setData(userInfoVo);
            }
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setStatus("fail");
            jsonResult.setMessage(e.getMessage());
        }
        return jsonResult;
    }

    @ApiOperation(value = "登录")
    @ResponseBody
    @PostMapping(value = "/login")
    public EntityJsonResult userLogin(@RequestBody Map<String,Object> userMap) {
        EntityJsonResult entityJsonResult = new EntityJsonResult("success","");
        try{
            String username = userMap.get("username").toString();
            String password = userMap.get("pwd").toString();
            password = Base64Util.EncoderByMd5(password);
            UserInfoExample example = new UserInfoExample();
            example.createCriteria().andUsernameEqualTo(username).andPwdEqualTo(password);
            UserInfoVo userInfo = userInfoService.selectForLogin(example);
            if(null != userInfo){
                userInfo.setToken(JwtUtil.createToken(userInfo.getId(), userInfo.getUsername()));
                entityJsonResult.setData(userInfo);
            }else{
                entityJsonResult.setStatus("fail");
                entityJsonResult.setMessage("登录失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            entityJsonResult.setStatus("fail");
            entityJsonResult.setMessage(e.getMessage());
        }
        return entityJsonResult;
    }

    @ApiOperation(value = "后台登录")
    @ResponseBody
    @PostMapping(value = "/backlogin")
    public EntityJsonResult backlogin(@RequestBody Map<String,String> userMap) {
        EntityJsonResult entityJsonResult = new EntityJsonResult("success","");
        try{
            String password = userMap.get("pwd").toString();
            password = Base64Util.EncoderByMd5(password);
            userMap.put("pwd",password);
            UserInfoVo userInfo = userInfoService.selectForbacklogin(userMap);
            if(null != userInfo){
                userInfo.setToken(JwtUtil.createToken(userInfo.getId(),userInfo.getUsername()));
                entityJsonResult.setData(userInfo);
            }else{
                entityJsonResult.setStatus("fail");
                entityJsonResult.setMessage("登录失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            entityJsonResult.setStatus("fail");
            entityJsonResult.setMessage(e.getMessage());
        }
        return entityJsonResult;
    }
}
