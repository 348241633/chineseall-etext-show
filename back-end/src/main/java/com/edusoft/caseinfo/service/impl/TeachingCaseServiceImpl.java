package com.edusoft.caseinfo.service.impl;

import com.common.util.PageQuerySetUtil;
import com.edusoft.caseinfo.mapper.TeachingCaseInfoMapper;
import com.edusoft.caseinfo.model.TeachingCaseInfo;
import com.edusoft.caseinfo.model.TeachingCaseInfoExample;
import com.edusoft.caseinfo.model.TeachingCaseInfoKey;
import com.edusoft.caseinfo.service.TeachingCaseService;
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
public class TeachingCaseServiceImpl implements TeachingCaseService {
    @Autowired
    private TeachingCaseInfoMapper teachingCaseInfoMapper;
    @Override
    public int insert(TeachingCaseInfo record) {
        return teachingCaseInfoMapper.insert(record);
    }

    @Override
    public int deleteByPrimaryKey(TeachingCaseInfoKey key) {
        return teachingCaseInfoMapper.deleteByPrimaryKey(key);
    }

    @Override
    public int updateByPrimaryKeySelective(TeachingCaseInfo record) {
        return teachingCaseInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByExampleSelective(@Param("record") TeachingCaseInfo record, @Param("example") TeachingCaseInfoExample example) {
        return teachingCaseInfoMapper.updateByExampleSelective(record,example);
    }

    @Override
    public List<TeachingCaseInfo> selectByExampleWithRowbounds(TeachingCaseInfoExample example, Map<String, Object> rowBounds) {
        PageQuerySetUtil.setPageQuery(rowBounds);
        return teachingCaseInfoMapper.selectByExample(example);
    }

    @Override
    public TeachingCaseInfo selectByPrimaryKey(TeachingCaseInfoKey key) {
        return teachingCaseInfoMapper.selectByPrimaryKey(key);
    }
}
