package com.edusoft.sysmanage.service;

import com.edusoft.sysmanage.model.UserInfo;
import com.edusoft.sysmanage.model.UserInfoExample;
import com.edusoft.sysmanage.model.UserInfoKey;
import com.edusoft.sysmanage.vo.OauthUserInfo;
import com.edusoft.sysmanage.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by lego-jspx01 on 2018/6/9.
 */
public interface UserInfoService {
    int insert(UserInfo record);

    UserInfoVo selectForLogin(UserInfoExample example);

    UserInfoVo selectForbacklogin(Map<String,String> map);

    UserInfo selectByPrimaryKey(UserInfoKey key);

    int updateByExampleSelective(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    int updateByPrimaryKeySelective(UserInfo record);

    List<UserInfo> selectByExample(UserInfoExample example);

    List<UserInfo> selectByExampleWithRowbounds(UserInfoExample example,  Map<String,Object> rowBounds);

    UserInfoVo selectOrInsertLoginUser(OauthUserInfo oauthUserInfo);
}
