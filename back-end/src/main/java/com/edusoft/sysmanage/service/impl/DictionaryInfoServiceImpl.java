package com.edusoft.sysmanage.service.impl;

import com.edusoft.sysmanage.mapper.DictionaryInfoMapper;
import com.edusoft.sysmanage.model.DictionaryInfo;
import com.edusoft.sysmanage.model.DictionaryInfoExample;
import com.edusoft.sysmanage.model.DictionaryInfoKey;
import com.edusoft.sysmanage.service.DictionaryInfoService;
import com.edusoft.sysmanage.vo.DictInfoVo;
import com.common.util.PageQuerySetUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by lego-jspx01 on 2018/6/9.
 */
@Service
@Transactional
public class DictionaryInfoServiceImpl implements DictionaryInfoService {
    @Autowired
    private DictionaryInfoMapper dictionaryInfoMapper;

    @Override
    public int insert(DictionaryInfo record) {
        return dictionaryInfoMapper.insert(record);
    }

    @Override
    public List<DictionaryInfo> selectByExampleWithRowbounds(DictionaryInfoExample example, Map<String,Object> map) {
        PageQuerySetUtil.setPageQuery(map);
        return dictionaryInfoMapper.selectByExample(example);
    }

    @Override
    public List<DictionaryInfo> selectByExample(DictionaryInfoExample example) {
        return dictionaryInfoMapper.selectByExample(example);
    }

    @Override
    public DictionaryInfo selectByPrimaryKey(DictionaryInfoKey key) {
        return dictionaryInfoMapper.selectByPrimaryKey(key);
    }

    @Override
    public int updateByExampleSelective(@Param("record") DictionaryInfo record, @Param("example") DictionaryInfoExample example) {
        return dictionaryInfoMapper.updateByExampleSelective(record, example);
    }


    @Override
    public int updateByPrimaryKeySelective(DictionaryInfo record) {
        return dictionaryInfoMapper.updateByPrimaryKeySelective(record);
    }


    /**
     * 获取所有字典信息
     * "dict_flag": [{
     *    "name": "未通过",
     *    "value": "2"
     *    },
     *    {
     *    "name": "撤销",
     *    "value": "-1"
     *    }]
     * @return
     */
    @Override
    public Map<String, List<DictInfoVo>> selectAllDict() {
        DictionaryInfoExample example = new DictionaryInfoExample();
        example.setOrderByClause(" dict_flag asc");
        List<DictionaryInfo> dictionarys = this.selectByExample(example);
        Map<String,List<DictInfoVo>> map = new HashMap<String,List<DictInfoVo>>();
        List<DictInfoVo> sublist = new ArrayList<DictInfoVo>();
        DictInfoVo dictInfoVo = null;
        DictionaryInfo dictionary =null;
        String key = null;
        for(int i=0;i<dictionarys.size();i++){
            dictionary = dictionarys.get(i);
            key = dictionary.getDictFlag();
            dictInfoVo = new DictInfoVo(dictionary.getDictText(),dictionary.getDictValue(),dictionary.getId(),dictionary.getPid());
            sublist.add(dictInfoVo);
            if(i!=dictionarys.size()-1){
                if(!key.equals(dictionarys.get(i+1).getDictFlag())){
                    map.put(key,sublist);
                    sublist = new ArrayList<DictInfoVo>();
                    continue;
                }else{

                }
            }else{//最后一个记录
                map.put(key,sublist);
            }
        }
        return map;
    }

    /**
     * 获取所有字典信息
     * "dict_flag": [{
     *    "name": "未通过",
     *    "value": "2"
     *    },
     *    {
     *    "name": "撤销",
     *    "value": "-1"
     *    }]
     * @return
     */
    @Override
    public Map<String, List<DictInfoVo>> selectSubDict(String dictFlag) {
        List<String> dictFlags = Arrays.asList(dictFlag.split(","));
        DictionaryInfoExample example = new DictionaryInfoExample();
        example.setOrderByClause(" dict_flag asc");
        example.createCriteria().andDictFlagIn(dictFlags);
        List<DictionaryInfo> dictionarys = this.selectByExample(example);
        Map<String,List<DictInfoVo>> map = new HashMap<String,List<DictInfoVo>>();
        List<DictInfoVo> sublist = new ArrayList<DictInfoVo>();
        DictInfoVo dictInfoVo = null;
        DictionaryInfo dictionary =null;
        String key = null;
        for(int i=0;i<dictionarys.size();i++){
            dictionary = dictionarys.get(i);
            key = dictionary.getDictFlag();
            dictInfoVo = new DictInfoVo(dictionary.getDictText(),dictionary.getDictValue(),dictionary.getId(),dictionary.getPid());
            sublist.add(dictInfoVo);
            if(i!=dictionarys.size()-1){
                if(!key.equals(dictionarys.get(i+1).getDictFlag())){
                    map.put(key,sublist);
                    sublist = new ArrayList<DictInfoVo>();
                    continue;
                }
            }else{//最后一个记录
                map.put(key,sublist);
            }
        }
        return map;
    }

    /**
     * 获取所有字典信息
     * key:"dict_flag:dict_value"
     * value:"dict_text"
     * @return
     */
    @Override
    public Map<String, String> selectAllSubDict(String dictFlag) {
//        DictionaryInfoExample example = new DictionaryInfoExample();
//        example.createCriteria().andDictFlagEqualTo(dictFlag);
        List<String> dictFlags = Arrays.asList(dictFlag.split(","));
        DictionaryInfoExample example = new DictionaryInfoExample();
        example.setOrderByClause(" dict_flag asc");
        example.createCriteria().andDictFlagIn(dictFlags);
        List<DictionaryInfo> dictionarys = this.selectByExample(example);
        Map<String, String> map = new HashMap<String, String>();
        for(DictionaryInfo dictionary : dictionarys) {
            map.put(dictionary.getDictFlag()+":"+dictionary.getDictValue(),dictionary.getDictText());
        }
        return map;
    }

    @Override
    public Map<String, String> selectAllSubDictByName(String dictFlag) {
        List<String> dictFlags = Arrays.asList(dictFlag.split(","));
        DictionaryInfoExample example = new DictionaryInfoExample();
        example.setOrderByClause(" dict_flag asc");
        example.createCriteria().andDictFlagIn(dictFlags);
        List<DictionaryInfo> dictionarys = this.selectByExample(example);
        Map<String, String> map = new HashMap<String, String>();
        for(DictionaryInfo dictionary : dictionarys) {
            map.put(dictionary.getDictFlag()+":"+dictionary.getDictText(),dictionary.getDictValue());
        }
        return map;
    }


}
