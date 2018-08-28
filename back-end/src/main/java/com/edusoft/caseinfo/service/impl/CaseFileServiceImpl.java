package com.edusoft.caseinfo.service.impl;

import com.edusoft.caseinfo.mapper.CaseDetailFileMapper;
import com.edusoft.caseinfo.model.CaseDetailFile;
import com.edusoft.caseinfo.model.CaseDetailFileExample;
import com.edusoft.caseinfo.model.CaseDetailFileKey;
import com.edusoft.caseinfo.service.CaseFileService;
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
public class CaseFileServiceImpl implements CaseFileService {
    @Autowired
    private CaseDetailFileMapper caseDetailFileMapper;
    @Override
    public int insert(CaseDetailFile record) {
        return caseDetailFileMapper.insert(record);
    }

    @Override
    public int insertList(List<CaseDetailFile> record) {
        return caseDetailFileMapper.insertList(record);
    }

    @Override
    public int deleteByPrimaryKey(CaseDetailFileKey key) {
        return caseDetailFileMapper.deleteByPrimaryKey(key);
    }

    @Override
    public int updateByPrimaryKeySelective(CaseDetailFile record) {
        return caseDetailFileMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByExampleSelective(@Param("record") CaseDetailFile record, @Param("example") CaseDetailFileExample example) {
        return caseDetailFileMapper.updateByExampleSelective(record,example);
    }


    @Override
    public List<CaseDetailFile> selectByExample(CaseDetailFileExample example) {
        return caseDetailFileMapper.selectByExample(example);
    }

    @Override
    public CaseDetailFile selectByPrimaryKey(CaseDetailFileKey key) {
        return caseDetailFileMapper.selectByPrimaryKey(key);
    }
}
