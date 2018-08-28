package com.edusoft.caseinfo.service;

import com.edusoft.caseinfo.model.CaseDetailFile;
import com.edusoft.caseinfo.model.CaseDetailFileExample;
import com.edusoft.caseinfo.model.CaseDetailFileKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
/**
 * Created by lego-jspx01 on 2018/8/22.
 */
public interface CaseFileService {
    int insert(CaseDetailFile record);

    int insertList(List<CaseDetailFile> record);

    int deleteByPrimaryKey(CaseDetailFileKey key);

    int updateByPrimaryKeySelective(CaseDetailFile record);

    int updateByExampleSelective(@Param("record") CaseDetailFile record, @Param("example") CaseDetailFileExample example);

    List<CaseDetailFile> selectByExample(CaseDetailFileExample example);

    CaseDetailFile selectByPrimaryKey(CaseDetailFileKey key);
}
