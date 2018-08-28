package com.edusoft.sysmanage.controller;

import com.edusoft.sysmanage.model.DictionaryInfo;
import com.edusoft.sysmanage.model.DictionaryInfoExample;
import com.edusoft.sysmanage.model.DictionaryInfoKey;
import com.edusoft.sysmanage.service.DataSyncService;
import com.edusoft.sysmanage.service.DictionaryInfoService;
import com.common.security.TokenUtil;
import com.common.util.Constants;
import com.common.util.DateUtils;
import com.common.util.resultjson.EntityJsonResult;
import com.common.util.resultjson.JsonResult;
import com.common.util.resultjson.ListJsonResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018/6/10.
 */
@Api(value = "字典管理", description = "字典管理API",tags = "DictApi", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping("/dictionary")
public class DictionaryInfoController {
    private static final Log log = LogFactory.getLog(DictionaryInfoController.class);

    @Autowired
    DictionaryInfoService dictionaryInfoService;
    @Autowired
    DataSyncService dataSyncService;

    @ApiOperation(value = "字典列表")
    @ResponseBody
    @GetMapping(value = "/list")
    public ListJsonResult listEntitys(@RequestParam("offset") int offset) {
        Map<String,Object> map = new HashMap<>();
        map.put("offset",offset);
        List<DictionaryInfo> list = dictionaryInfoService.selectByExampleWithRowbounds(null,map);
        PageInfo<DictionaryInfo> page = new PageInfo<DictionaryInfo>(list);
        ListJsonResult json = new ListJsonResult("success","success msg");
        json.setData(page.getList());
        json.setTotal(page.getTotal());
        return json;
    }


    @ApiOperation(value = "字典详情")
    @ResponseBody
    @GetMapping(value = "/detail/{id}")
    public EntityJsonResult getEntity(@PathVariable Integer id) {
        EntityJsonResult entityJsonResult = new EntityJsonResult("success","");
        try{
            if(id==0){
                entityJsonResult.setStatus("fail");
                entityJsonResult.setMessage("字典id不可为空");
                return entityJsonResult;
            }
            DictionaryInfoKey key = new DictionaryInfoKey();
            key.setId(id);
            DictionaryInfo dictionaryInfo = dictionaryInfoService.selectByPrimaryKey(key);
            entityJsonResult.setData(dictionaryInfo);
        }catch (Exception e){
            e.printStackTrace();
            entityJsonResult.setStatus("fail");
            entityJsonResult.setMessage(e.getMessage());
        }
        return entityJsonResult;
    }

    @ApiOperation(value = "新增字典")
    @ResponseBody
    @PostMapping(value = "/add")
    public JsonResult addEntity(@RequestBody DictionaryInfo dictionaryInfo){
        JsonResult jsonResult = new JsonResult("success","");
        try{
            dictionaryInfo.setAddUser(TokenUtil.getUserName());
            dictionaryInfo.setDeleted(Constants.DELETE_DEFAULT);
            dictionaryInfo.setAddTime(DateUtils.getNowDate());
            dictionaryInfoService.insert(dictionaryInfo);
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setStatus("fail");
            jsonResult.setMessage(e.getMessage());
        }
        return jsonResult;
    }

    @ApiOperation(value = "修改字典")
    @ResponseBody
    @PutMapping(value = "/update/{id}")
    public JsonResult updateEntity(@PathVariable Integer id,@RequestBody DictionaryInfo dictionaryInfo) throws Exception{
        JsonResult jsonResult = new JsonResult("success","");
        try{
            if(id==0){
                jsonResult.setStatus("fail");
                jsonResult.setMessage("字典id不可为空");
                return jsonResult;
            }
            dictionaryInfo.setId(id);
            dictionaryInfo.setUpdateUser(TokenUtil.getUserName());
            dictionaryInfo.setUpdateTime(DateUtils.getNowDate());
            DictionaryInfoExample example = new DictionaryInfoExample();
            example.createCriteria().andIdEqualTo(id);
            dictionaryInfoService.updateByExampleSelective(dictionaryInfo, example);
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setStatus("fail");
            jsonResult.setMessage(e.getMessage());
        }
        return jsonResult;
    }

    @ApiOperation(value = "逻辑删除字典")
    @ResponseBody
    @DeleteMapping(value = "/delete/{id}")
    public JsonResult deleteEntity(@PathVariable Integer id) {
        JsonResult jsonResult = new JsonResult("success","");
        try{
            if(id==0){
                jsonResult.setStatus("fail");
                jsonResult.setMessage("字典id不可为空");
                return jsonResult;
            }
            DictionaryInfo dictionaryInfo = new DictionaryInfo();
            dictionaryInfo.setId(id);
            dictionaryInfo.setDeleted(Constants.IS_DELETED);
            dictionaryInfoService.updateByPrimaryKeySelective(dictionaryInfo);
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setStatus("fail");
            jsonResult.setMessage(e.getMessage());
        }
        return jsonResult;
    }

    @ApiOperation(value = "所有字典json")
    @ResponseBody
    @GetMapping(value = "/allDictJson")
    public EntityJsonResult allDictJson() {
        EntityJsonResult entityJsonResult = new EntityJsonResult("success","");
        try{
            entityJsonResult.setData(dictionaryInfoService.selectAllDict());
        }catch (Exception e){
            e.printStackTrace();
            entityJsonResult.setStatus("fail");
            entityJsonResult.setMessage(e.getMessage());
        }
        return entityJsonResult;
    }

    @ApiOperation(value = "所有字典列表json")
    @ResponseBody
    @GetMapping(value = "/list/{dictFlag}")
    public EntityJsonResult sublistEntitys(@PathVariable String dictFlag) {
        EntityJsonResult entityJsonResult = new EntityJsonResult("success","");
        try{
            if(StringUtils.isEmpty(dictFlag)){
                entityJsonResult.setStatus("fail");
                entityJsonResult.setMessage("字典标识不可为空");
                return entityJsonResult;
            }
            entityJsonResult.setData(dictionaryInfoService.selectAllSubDict(dictFlag));
        }catch (Exception e){
            e.printStackTrace();
            entityJsonResult.setStatus("fail");
            entityJsonResult.setMessage(e.getMessage());
        }
        return entityJsonResult;
    }

    @ApiOperation(value = "某些字典类型的字典json")
    @ResponseBody
    @GetMapping(value = "/allDictJson/{dictFlag}")
    public EntityJsonResult allDictJsonByFlag(@PathVariable String dictFlag) {
        EntityJsonResult entityJsonResult = new EntityJsonResult("success","");
        try{
            if(StringUtils.isEmpty(dictFlag)){
                entityJsonResult.setStatus("fail");
                entityJsonResult.setMessage("字典标识不可为空");
                return entityJsonResult;
            }
            entityJsonResult.setData(dictionaryInfoService.selectSubDict(dictFlag));
        }catch (Exception e){
            e.printStackTrace();
            entityJsonResult.setStatus("fail");
            entityJsonResult.setMessage(e.getMessage());
        }
        return entityJsonResult;
    }

    @ResponseBody
    @GetMapping(value = "/testdict")
    public EntityJsonResult testdict() {
        EntityJsonResult entityJsonResult = new EntityJsonResult("success","");
        dataSyncService.syncDictInfo();
        return entityJsonResult;
    }

}
