package com.edusoft.sysmanage.service;

import com.edusoft.sysmanage.model.*;
import com.edusoft.sysmanage.vo.OauthUserInfo;
import com.edusoft.sysmanage.vo.UserInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by lego-jspx01 on 2018/6/9.
 */
public interface NoticeInfoService {
    int insert(NoticeInfoWithBLOBs record);

    int deleteByPrimaryKey(NoticeInfoKey key);

    int updateByPrimaryKeySelective(NoticeInfoWithBLOBs record);

    int updateByExampleSelective(@Param("record") NoticeInfoWithBLOBs record, @Param("example") NoticeInfoExample example);

    List<NoticeInfo> selectByExampleWithRowbounds(NoticeInfoExample example, Map<String, Object> rowBounds);

    NoticeInfoWithBLOBs selectByPrimaryKey(NoticeInfoKey key);
}
