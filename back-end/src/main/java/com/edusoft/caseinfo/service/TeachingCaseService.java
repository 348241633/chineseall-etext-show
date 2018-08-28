package com.edusoft.caseinfo.service;
import com.edusoft.caseinfo.model.TeachingCaseInfo;
import com.edusoft.caseinfo.model.TeachingCaseInfoExample;
import com.edusoft.caseinfo.model.TeachingCaseInfoKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
/**
 * Created by lego-jspx01 on 2018/8/22.
 */
public interface TeachingCaseService {
    int insert(TeachingCaseInfo record);

    int deleteByPrimaryKey(TeachingCaseInfoKey key);

    int updateByPrimaryKeySelective(TeachingCaseInfo record);

    int updateByExampleSelective(@Param("record") TeachingCaseInfo record, @Param("example") TeachingCaseInfoExample example);

    List<TeachingCaseInfo> selectByExampleWithRowbounds(TeachingCaseInfoExample example, Map<String, Object> rowBounds);

    TeachingCaseInfo selectByPrimaryKey(TeachingCaseInfoKey key);
}
