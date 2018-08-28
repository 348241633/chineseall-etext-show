package com.edusoft.sysmanage.service;


import com.edusoft.sysmanage.model.DictionaryInfo;
import com.edusoft.sysmanage.model.DictionaryInfoExample;
import com.edusoft.sysmanage.model.DictionaryInfoKey;
import com.edusoft.sysmanage.vo.DictInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/10.
 */
public interface DictionaryInfoService {

    int insert(DictionaryInfo record);

    List<DictionaryInfo> selectByExampleWithRowbounds(DictionaryInfoExample example, Map<String,Object> map);

    List<DictionaryInfo> selectByExample(DictionaryInfoExample example);

    DictionaryInfo selectByPrimaryKey(DictionaryInfoKey key);

    int updateByExampleSelective(@Param("record") DictionaryInfo record, @Param("example") DictionaryInfoExample example);

    int updateByPrimaryKeySelective(DictionaryInfo record);

    Map<String,List<DictInfoVo>> selectAllDict();

    Map<String,List<DictInfoVo>> selectSubDict(String dictFlag);

    Map<String, String> selectAllSubDict(String dictFlag);
    Map<String, String> selectAllSubDictByName(String dictFlag);

}
