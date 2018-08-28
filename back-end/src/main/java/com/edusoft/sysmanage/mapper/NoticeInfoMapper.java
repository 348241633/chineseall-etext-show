package com.edusoft.sysmanage.mapper;

import com.edusoft.sysmanage.model.NoticeInfo;
import com.edusoft.sysmanage.model.NoticeInfoExample;
import com.edusoft.sysmanage.model.NoticeInfoKey;
import com.edusoft.sysmanage.model.NoticeInfoWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface NoticeInfoMapper {
    int countByExample(NoticeInfoExample example);

    int deleteByExample(NoticeInfoExample example);

    int deleteByPrimaryKey(NoticeInfoKey key);

    int insert(NoticeInfoWithBLOBs record);

    int insertSelective(NoticeInfoWithBLOBs record);

    List<NoticeInfoWithBLOBs> selectByExampleWithBLOBsWithRowbounds(NoticeInfoExample example, RowBounds rowBounds);

    List<NoticeInfoWithBLOBs> selectByExampleWithBLOBs(NoticeInfoExample example);

    List<NoticeInfo> selectByExampleWithRowbounds(NoticeInfoExample example, RowBounds rowBounds);

    List<NoticeInfo> selectByExample(NoticeInfoExample example);

    NoticeInfoWithBLOBs selectByPrimaryKey(NoticeInfoKey key);

    int updateByExampleSelective(@Param("record") NoticeInfoWithBLOBs record, @Param("example") NoticeInfoExample example);

    int updateByExampleWithBLOBs(@Param("record") NoticeInfoWithBLOBs record, @Param("example") NoticeInfoExample example);

    int updateByExample(@Param("record") NoticeInfo record, @Param("example") NoticeInfoExample example);

    int updateByPrimaryKeySelective(NoticeInfoWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(NoticeInfoWithBLOBs record);

    int updateByPrimaryKey(NoticeInfo record);
}