package com.edusoft.caseinfo.mapper;

import com.edusoft.caseinfo.model.CaseDetailFile;
import com.edusoft.caseinfo.model.CaseDetailFileExample;
import com.edusoft.caseinfo.model.CaseDetailFileKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface CaseDetailFileMapper {
    int countByExample(CaseDetailFileExample example);

    int deleteByExample(CaseDetailFileExample example);

    int deleteByPrimaryKey(CaseDetailFileKey key);

    int insert(CaseDetailFile record);

    int insertList(List<CaseDetailFile> record);

    int insertSelective(CaseDetailFile record);

    List<CaseDetailFile> selectByExampleWithRowbounds(CaseDetailFileExample example, RowBounds rowBounds);

    List<CaseDetailFile> selectByExample(CaseDetailFileExample example);

    CaseDetailFile selectByPrimaryKey(CaseDetailFileKey key);

    int updateByExampleSelective(@Param("record") CaseDetailFile record, @Param("example") CaseDetailFileExample example);

    int updateByExample(@Param("record") CaseDetailFile record, @Param("example") CaseDetailFileExample example);

    int updateByPrimaryKeySelective(CaseDetailFile record);

    int updateByPrimaryKey(CaseDetailFile record);
}