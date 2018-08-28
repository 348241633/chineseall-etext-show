package com.edusoft.caseinfo.mapper;

import com.edusoft.caseinfo.model.TeachingCaseInfo;
import com.edusoft.caseinfo.model.TeachingCaseInfoExample;
import com.edusoft.caseinfo.model.TeachingCaseInfoKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface TeachingCaseInfoMapper {
    int countByExample(TeachingCaseInfoExample example);

    int deleteByExample(TeachingCaseInfoExample example);

    int deleteByPrimaryKey(TeachingCaseInfoKey key);

    int insert(TeachingCaseInfo record);

    int insertSelective(TeachingCaseInfo record);

    List<TeachingCaseInfo> selectByExampleWithRowbounds(TeachingCaseInfoExample example, RowBounds rowBounds);

    List<TeachingCaseInfo> selectByExample(TeachingCaseInfoExample example);

    TeachingCaseInfo selectByPrimaryKey(TeachingCaseInfoKey key);

    int updateByExampleSelective(@Param("record") TeachingCaseInfo record, @Param("example") TeachingCaseInfoExample example);

    int updateByExample(@Param("record") TeachingCaseInfo record, @Param("example") TeachingCaseInfoExample example);

    int updateByPrimaryKeySelective(TeachingCaseInfo record);

    int updateByPrimaryKey(TeachingCaseInfo record);
}