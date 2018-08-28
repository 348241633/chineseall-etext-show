package com.edusoft.sysmanage.mapper;

import com.edusoft.sysmanage.model.DictionaryInfo;
import com.edusoft.sysmanage.model.DictionaryInfoExample;
import com.edusoft.sysmanage.model.DictionaryInfoKey;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface DictionaryInfoMapper {
    int countByExample(DictionaryInfoExample example);

    int deleteByExample(DictionaryInfoExample example);

    int deleteByPrimaryKey(DictionaryInfoKey key);

    int insert(DictionaryInfo record);

    int insertSelective(DictionaryInfo record);

    List<DictionaryInfo> selectByExampleWithRowbounds(DictionaryInfoExample example, RowBounds rowBounds);

    List<DictionaryInfo> selectByExample(DictionaryInfoExample example);

    DictionaryInfo selectByPrimaryKey(DictionaryInfoKey key);

    int updateByExampleSelective(@Param("record") DictionaryInfo record, @Param("example") DictionaryInfoExample example);

    int updateByExample(@Param("record") DictionaryInfo record, @Param("example") DictionaryInfoExample example);

    int updateByPrimaryKeySelective(DictionaryInfo record);

    int updateByPrimaryKey(DictionaryInfo record);


    //同步相关方法
    int insertSyncLog(Map<String,Object> map);
    int truncateSyncMid(@Param("tableName")String tableName);
    int insertSyncMidByDict(List<Map> list);
    int insertSyncMidByBookInfo(List<Map> list);
    int syncBookInfo();
    int syncupdateBookInfo();
    int syncDictInfo(Map<String,Object> map);
    int syncUpdateDictInfo();
    List<Map<String,String>> getAllBookInfo(Map<String,Object> map);
}