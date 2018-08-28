package com.edusoft.caseinfo.controller;

import com.common.security.TokenUtil;
import com.common.util.Base64Util;
import com.common.util.Constants;
import com.common.util.DateUtils;
import com.common.util.resultjson.EntityJsonResult;
import com.common.util.resultjson.JsonResult;
import com.common.util.resultjson.ListJsonResult;
import com.edusoft.caseinfo.model.*;
import com.edusoft.caseinfo.service.CaseFileService;
import com.edusoft.caseinfo.service.TeachingCaseService;
import com.edusoft.caseinfo.vo.TeachingCaseInfoVo;
import com.edusoft.sysmanage.service.DictionaryInfoService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lego-jspx01 on 2018/6/9.
 */
@Api(value = "教学案列管理", description = "教学案列管理API",tags = "caseinfoApi", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping("/caseinfo")
public class TeachingCaseController {
    private static final Log log = LogFactory.getLog(TeachingCaseController.class);
    @Autowired
    CaseFileService caseFileService;
    @Autowired
    TeachingCaseService teachingCaseService;
    @Autowired
    DictionaryInfoService dictionaryInfoService;

    @ApiOperation(value = "前台首页教学案列列表")
    @ResponseBody
    @GetMapping(value = "/index")
    public ListJsonResult resourceIndex(@RequestParam("offset") Integer offset
            ,@RequestParam(defaultValue = "10") Integer limit
            ,@RequestParam(required = false) String keyWords
            ,@RequestParam(required = false) String grade
            ,@RequestParam(required = false) String subject
            ,@RequestParam(defaultValue = "id") String orderByFiled
            ,@RequestParam(defaultValue = "desc") String orderByType
    ) {
        Map<String,Object> map = new HashMap<>();
        map.put("offset", offset);
        map.put("limit", limit);
        TeachingCaseInfoExample example = new TeachingCaseInfoExample();
        TeachingCaseInfoExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(keyWords)){
            criteria.andTopicNameLike(keyWords);
        }
        if(!StringUtils.isEmpty(grade)){
            String grades[]= grade.split(",");
            criteria.andSubjectIn(Arrays.asList(grades));
        }
        if(!StringUtils.isEmpty(subject)){
            String subjects[]= subject.split(",");
            criteria.andSubjectIn(Arrays.asList(subjects));
        }
        criteria.andStatusEqualTo("1");//已发布的案列
        orderByFiled = Base64Util.getLowerField("a.",orderByFiled);
        map.put("orderByClause",orderByFiled +" "+orderByType);
        ListJsonResult json = new ListJsonResult("success","");
        try{
            List<TeachingCaseInfo> uplist = teachingCaseService.selectByExampleWithRowbounds(example,map);
            PageInfo<TeachingCaseInfo> page = new PageInfo<TeachingCaseInfo>(uplist);
            json.setData(page.getList());
            json.setTotal(page.getTotal());
        }catch (Exception e){
            e.printStackTrace();
            json.setStatus("fail");
            json.setMessage("操作错误："+e.getMessage());
        }
        return json;
    }

    @ApiOperation(value = "教学案列管理列表")
    @ResponseBody
    @GetMapping(value = "/list")
    public ListJsonResult resourceList(@RequestParam("offset") Integer offset
            ,@RequestParam(defaultValue = "10") Integer limit
            ,@RequestParam(required = false) String keyWords
            ,@RequestParam(required = false) String grade
            ,@RequestParam(required = false) String subject
            ,@RequestParam(defaultValue = "id") String orderByFiled
            ,@RequestParam(defaultValue = "desc") String orderByType
    ) {
        Map<String,Object> map = new HashMap<>();
        map.put("offset", offset);
        map.put("limit", limit);
        TeachingCaseInfoExample example = new TeachingCaseInfoExample();
        TeachingCaseInfoExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(keyWords)){
            criteria.andTopicNameLike(keyWords);
        }
        if(!StringUtils.isEmpty(grade)){
            String grades[]= grade.split(",");
            criteria.andSubjectIn(Arrays.asList(grades));
        }
        if(!StringUtils.isEmpty(subject)){
            String subjects[]= subject.split(",");
            criteria.andSubjectIn(Arrays.asList(subjects));
        }
        orderByFiled = Base64Util.getLowerField("a.",orderByFiled);
        map.put("orderByClause",orderByFiled +" "+orderByType);
        ListJsonResult json = new ListJsonResult("success","");
        try{
            List<TeachingCaseInfo> uplist = teachingCaseService.selectByExampleWithRowbounds(example,map);
            PageInfo<TeachingCaseInfo> page = new PageInfo<TeachingCaseInfo>(uplist);
            json.setData(page.getList());
            json.setTotal(page.getTotal());
        }catch (Exception e){
            e.printStackTrace();
            json.setStatus("fail");
            json.setMessage("操作错误："+e.getMessage());
        }
        return json;
    }



    @ApiOperation(value = "教学案列详情")
    @ResponseBody
    @GetMapping(value = "/detail/{id}")
    public EntityJsonResult caseInfoDetail(@PathVariable Integer id) {
        EntityJsonResult entityJsonResult = new EntityJsonResult("success","");
        try{
            TeachingCaseInfoKey key = new TeachingCaseInfoKey();
            key.setId(id);
            TeachingCaseInfo resourceInfo = teachingCaseService.selectByPrimaryKey(key);
            Map<String,String> dictMap = dictionaryInfoService.selectAllSubDict("t_recource_type,t_xueke,t_grade_info,t_term_info");
            resourceInfo.setSubject(dictMap.get("t_xueke:" + resourceInfo.getSubject()));
            resourceInfo.setGrade(dictMap.get("t_grade_info:" + resourceInfo.getGrade()));
            resourceInfo.setTerm(dictMap.get("t_term_info:" + resourceInfo.getTerm()));
            TeachingCaseInfoVo teachingCaseInfoVo = new TeachingCaseInfoVo();
            BeanUtils.copyProperties(resourceInfo, teachingCaseInfoVo);
            CaseDetailFileExample example = new CaseDetailFileExample();
            example.createCriteria().andCaseIdEqualTo(id);
            List<CaseDetailFile > list = caseFileService.selectByExample(example);
            teachingCaseInfoVo.setCaseDetailFile(list);
            entityJsonResult.setData(teachingCaseInfoVo);
        }catch (Exception e){
            e.printStackTrace();
            entityJsonResult.setStatus("fail");
            entityJsonResult.setMessage("操作错误："+e.getMessage());
        }
        return entityJsonResult;
    }

    @ApiOperation(value = "修改教学案列基本信息")
    @ResponseBody
    @PutMapping(value = "/update/{id}")
    public JsonResult updateResource(@PathVariable Integer id,@RequestBody TeachingCaseInfo teachingCaseInfo) {
        JsonResult jsonResult = new JsonResult("success","");
        String username = TokenUtil.getUserName();
        try{
            teachingCaseInfo.setId(id);
            teachingCaseInfo.setUpdateTime(DateUtils.getNowDate());
            teachingCaseInfo.setUpdateUser(username);
            teachingCaseService.updateByPrimaryKeySelective(teachingCaseInfo);
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setStatus("fail");
            jsonResult.setMessage("操作错误："+e.getMessage());
        }
        return jsonResult;
    }

    @ApiOperation(value = "逻辑删除教学案列")
    @ResponseBody
    @DeleteMapping(value = "/delete/{id}")
    public JsonResult deleteResource(@PathVariable Integer id) {
        JsonResult jsonResult = new JsonResult("success","");
        try{
            TeachingCaseInfo resourceInfo = new TeachingCaseInfo();
            resourceInfo.setId(id);
            resourceInfo.setDeleted(Constants.IS_DELETED);
            teachingCaseService.updateByPrimaryKeySelective(resourceInfo);
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setStatus("fail");
            jsonResult.setMessage("操作错误："+e.getMessage());
        }
        return jsonResult;
    }

    @ApiOperation(value = "新增教学案列")
    @ResponseBody
    @PostMapping(value = "/add")
    public JsonResult addEntity(@RequestBody TeachingCaseInfo resourceInfo){
        JsonResult jsonResult = new JsonResult("success","");
        try{
            resourceInfo.setAddUser(TokenUtil.getUserName());
            resourceInfo.setDeleted(Constants.DELETE_DEFAULT);
            resourceInfo.setAddTime(DateUtils.getNowDate());
            resourceInfo.setStatus("0");
            teachingCaseService.insert(resourceInfo);
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setStatus("fail");
            jsonResult.setMessage("操作错误："+e.getMessage());
        }
        return jsonResult;
    }


    @ApiOperation(value = "教学案列上下架")
    @ResponseBody
    @PutMapping(value = "/upOrLower/{id}")
    public JsonResult upOrLower(@PathVariable Integer id,@RequestParam String status) {
        JsonResult jsonResult = new JsonResult("success","");
        try{
            if("0,1".indexOf(status)!=-1){
                String username = TokenUtil.getUserName();
                TeachingCaseInfo resourceInfo = new TeachingCaseInfo();
                resourceInfo.setStatus(status);
                resourceInfo.setId(id);
                resourceInfo.setUpdateTime(DateUtils.getNowDate());
                resourceInfo.setUpdateUser(username);
                teachingCaseService.updateByPrimaryKeySelective(resourceInfo);
            }else{
                jsonResult.setStatus("fail");
                jsonResult.setMessage("状态码填写有错误");
            }
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setStatus("fail");
            jsonResult.setMessage("操作错误："+e.getMessage());
        }
        return jsonResult;
    }

//    @ApiOperation(value = "教学案列文件信息添加(不包含文件主体)")
//    @ResponseBody
//    @PostMapping(value = "/{id}/detailFile/add")
//    public JsonResult addDetailFile(@PathVariable Integer id,@RequestBody CaseDetailFile caseDetailFile){
//        JsonResult jsonResult = new JsonResult("success","");
//        try{
//            caseDetailFile.setCaseId(id);
//            caseFileService.insert(caseDetailFile);
//        }catch (Exception e){
//            e.printStackTrace();
//            jsonResult.setStatus("fail");
//            jsonResult.setMessage("操作错误："+e.getMessage());
//        }
//        return jsonResult;
//    }
//
//    @ApiOperation(value = "教学案列文件属性批量添加(不包含文件主体)")
//    @ResponseBody
//    @PostMapping(value = "/{id}/detailFile/addList")
//    public JsonResult addDetailFile(@PathVariable Integer id,@RequestBody List<CaseDetailFile> list){
//        JsonResult jsonResult = new JsonResult("success","");
//        try{
//            for(CaseDetailFile obj : list){
//                obj.setCaseId(id);
//                obj.setDeleted(Constants.DELETE_DEFAULT);
//            }
//            caseFileService.insertList(list);
//        }catch (Exception e){
//            e.printStackTrace();
//            jsonResult.setStatus("fail");
//            jsonResult.setMessage("操作错误："+e.getMessage());
//        }
//        return jsonResult;
//    }




}
