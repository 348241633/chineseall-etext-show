package com.edusoft.sysmanage.service.impl;

import com.edusoft.sysmanage.mapper.UserInfoMapper;
import com.edusoft.sysmanage.model.UserInfo;
import com.edusoft.sysmanage.model.UserInfoExample;
import com.edusoft.sysmanage.model.UserInfoKey;
import com.edusoft.sysmanage.service.UserInfoService;
import com.edusoft.sysmanage.vo.OauthUserInfo;
import com.edusoft.sysmanage.vo.UserInfoVo;
import com.common.util.Constants;
import com.common.util.PageQuerySetUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by lego-jspx01 on 2018/6/9.
 */
@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Override
    public int insert(UserInfo record) {
        return userInfoMapper.insert(record);
    }

    @Override
    public UserInfoVo selectForLogin(UserInfoExample example) {
        return userInfoMapper.selectForLogin(example);
    }

    @Override
    public UserInfoVo selectForbacklogin(Map<String,String> map) {
        return userInfoMapper.selectForbacklogin(map);
    }

    @Override
    public UserInfo selectByPrimaryKey(UserInfoKey key) {
        return userInfoMapper.selectByPrimaryKey(key);
    }

    @Override
    public int updateByExampleSelective(@Param("record") UserInfo record, @Param("example") UserInfoExample example) {
        return userInfoMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(UserInfo record) {
        return userInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<UserInfo> selectByExample(UserInfoExample example) {
        return userInfoMapper.selectByExample(example);
    }

    @Override
    public List<UserInfo> selectByExampleWithRowbounds(UserInfoExample example,  Map<String,Object> rowBounds) {
        PageQuerySetUtil.setPageQuery(rowBounds);
        return userInfoMapper.selectByExample(example);
    }

    @Override
    public UserInfoVo selectOrInsertLoginUser(OauthUserInfo oauthUserInfo) {
        UserInfoExample example = new UserInfoExample();
        example.createCriteria().andCasUserIdEqualTo(oauthUserInfo.getUser_id());
        List<UserInfo> list = userInfoMapper.selectByExample(example);
        UserInfoVo userInfoVo = new UserInfoVo();
        if(null != list && list.size()>0){//本地有记录直接返回
            BeanUtils.copyProperties(list.get(0), userInfoVo);
        }else{
            UserInfo record = new UserInfo();
            record.setCasUserId(oauthUserInfo.getUser_id());
            record.setRealname(oauthUserInfo.getUser_name());
            record.setUsername(oauthUserInfo.getLogin_name());
            record.setGender(oauthUserInfo.getSex());
            record.setBirthday(oauthUserInfo.getBirthday());
            record.setUserType(oauthUserInfo.getUser_type());
            record.setTelNum(oauthUserInfo.getTel());
            record.setEmail(oauthUserInfo.getEmail());
            record.setStatus(Constants.DEFAULT_STATUS);
            userInfoMapper.insert(record);
            BeanUtils.copyProperties(record, userInfoVo);
        }
        userInfoVo.setAuths(oauthUserInfo.getAuths());
        return userInfoVo;
    }
}
