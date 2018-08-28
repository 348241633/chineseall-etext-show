package com.edusoft.sysmanage.service.impl;

import com.alibaba.fastjson.JSON;
import com.common.util.HttpClientUtil;
import com.edusoft.sysmanage.mapper.DictionaryInfoMapper;
import com.edusoft.sysmanage.service.DataSyncService;
import com.edusoft.sysmanage.vo.OauthUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 18/8/15.
 */
@Service
@Transactional
public class DataSyncServiceImpl implements DataSyncService {
    @Autowired
    private DictionaryInfoMapper dictionaryInfoMapper;
    @Value("${api.url}")
    private String apiUrl;
    @Value("${cas.oauth2.client}")
    private String clentId;
    private final static String[] dictFlag = {"zjlx","xbm","t_bz_xl","org_type","t_xueduan","xue_zhi","user_type","t_xueke","t_grade_info","t_term_info","t_jc_type","t_book_publish","t_course_type"};
    private final static String[] dictName = {"证件类型","性别","学历","机构类型","学段","学制","用户类型","学科","年级","学期","教材类型","出版社","课程类型"};
    private final static String[] dictCode = {"CB16","ZW08","ZW01","ZW02","ZW03","ZW04","ZW06","ZW07","ZW10","ZW17","ZW18","ZW19","ZW20"};


    @Override
    public void syncDictInfo() {
        Map<String,Object> logMap = new HashMap<>();
        logMap.put("type","alldict");
        dictionaryInfoMapper.truncateSyncMid("t_sync_dict_mid");
        String token = "";//this.getToken();
        String url = apiUrl + "/api/2/get_all_dic?clientId="+clentId;
        String result = HttpClientUtil.getUrl(token,url);
        Map<String,Object> resultMap = JSON.parseObject(result, Map.class);
        if(0 == Integer.parseInt(resultMap.get("code").toString())){
            List<Map> map =  JSON.parseArray(resultMap.get("data").toString(), Map.class);
            dictionaryInfoMapper.insertSyncMidByDict(map);
            Map<String,Object> paramap = new HashMap<>();
            for(int i=0;i<dictCode.length;i++){
                paramap.put("dictFlag",dictFlag[i]);
                paramap.put("type",dictCode[i]);
                paramap.put("desc",dictName[i]);
                dictionaryInfoMapper.syncDictInfo(paramap);
            }
            dictionaryInfoMapper.syncUpdateDictInfo();
        }else{
            logMap.put("result",resultMap.get("message"));
        }
        dictionaryInfoMapper.insertSyncLog(logMap);
    }

//    private String getToken(){
//        Map<String,Object> map = new HashMap<>();
//        map.put("login_way","1");
//        map.put("login_name","yygly");
//        map.put("password", HttpClientUtil.encodePassWord("111111"));
//        map.put("client_id",clentId);
//        String url = apiUrl + "/api/2/login";
//        String result = HttpClientUtil.postUrl(map,url);
//        if(null != result){
//            Map<String,Object> resultMap = JSON.parseObject(result, Map.class);
//            if(0 == Integer.parseInt(resultMap.get("code").toString())){
//                OauthUserInfo oauthUserInfo =  JSON.parseObject(resultMap.get("data").toString(), OauthUserInfo.class);
//                return oauthUserInfo.getToken();
//            }
//        }
//        return "";
//    }


}
