package com.edusoft.sysmanage.service.impl;

import com.common.util.PageQuerySetUtil;
import com.edusoft.sysmanage.mapper.NoticeInfoMapper;
import com.edusoft.sysmanage.model.NoticeInfo;
import com.edusoft.sysmanage.model.NoticeInfoExample;
import com.edusoft.sysmanage.model.NoticeInfoKey;
import com.edusoft.sysmanage.model.NoticeInfoWithBLOBs;
import com.edusoft.sysmanage.service.NoticeInfoService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by lego-jspx01 on 2018/8/22.
 */
@Service
@Transactional
public class NoticeInfoServiceImpl implements NoticeInfoService {

    @Autowired
    private NoticeInfoMapper noticeInfoMapper;
    @Override
    public int insert(NoticeInfoWithBLOBs record) {
        return noticeInfoMapper.insert(record);
    }

    @Override
    public int deleteByPrimaryKey(NoticeInfoKey key) {
        return noticeInfoMapper.deleteByPrimaryKey(key);
    }

    @Override
    public int updateByPrimaryKeySelective(NoticeInfoWithBLOBs record) {
        return noticeInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByExampleSelective(@Param("record") NoticeInfoWithBLOBs record, @Param("example") NoticeInfoExample example) {
        return noticeInfoMapper.updateByExampleSelective(record,example);
    }

    @Override
    public List<NoticeInfo> selectByExampleWithRowbounds(NoticeInfoExample example, Map<String, Object> rowBounds) {
        PageQuerySetUtil.setPageQuery(rowBounds);
        return noticeInfoMapper.selectByExample(example);
    }

    @Override
    public NoticeInfoWithBLOBs selectByPrimaryKey(NoticeInfoKey key) {
        return noticeInfoMapper.selectByPrimaryKey(key);
    }
}
