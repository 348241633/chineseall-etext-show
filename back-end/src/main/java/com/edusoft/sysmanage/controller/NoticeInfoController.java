package com.edusoft.sysmanage.controller;

import com.alibaba.fastjson.JSON;
import com.common.security.JwtUtil;
import com.common.security.TokenUtil;
import com.common.util.Base64Util;
import com.common.util.Constants;
import com.common.util.DateUtils;
import com.common.util.HttpClientUtil;
import com.common.util.resultjson.EntityJsonResult;
import com.common.util.resultjson.JsonResult;
import com.common.util.resultjson.ListJsonResult;
import com.edusoft.sysmanage.model.*;
import com.edusoft.sysmanage.service.NoticeInfoService;
import com.edusoft.sysmanage.service.UserInfoService;
import com.edusoft.sysmanage.vo.UserInfoVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lego-jspx01 on 2018/8/22.
 */
@Api(value = "通知公告管理", description = "通知公告API",tags = "NoticeInfoApi", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping("/notice")
public class NoticeInfoController {
    private static final Log log = LogFactory.getLog(NoticeInfoController.class);
    @Autowired
    NoticeInfoService noticeInfoService;

    @ApiOperation(value = "通知列表")
    @ResponseBody
    @GetMapping(value = "/list")
    public ListJsonResult noticList(@RequestParam(value="offset", defaultValue = "1") Integer offset,
                                   @RequestParam(defaultValue = "10") Integer limit,
                                   @RequestParam(required = false) String title) {
        Map<String,Object> map = new HashMap<>();
        map.put("offset", offset);
        map.put("limit", limit);
        NoticeInfoExample example = new NoticeInfoExample();
        NoticeInfoExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(title))
            criteria.andTitleLike(title);
        List<NoticeInfo> list = noticeInfoService.selectByExampleWithRowbounds(example,map);
        PageInfo<NoticeInfo> page = new PageInfo<NoticeInfo>(list);
        ListJsonResult json = new ListJsonResult("success","");
        json.setData(page.getList());
        json.setTotal(page.getTotal());
        return json;
    }

    @ApiOperation(value = "新增通知")
    @ResponseBody
    @PostMapping(value = "/add")
    public JsonResult addNoticeInfo(@RequestBody NoticeInfoWithBLOBs noticeInfo) {
        JsonResult jsonResult = new JsonResult("success","");
        try{
            noticeInfo.setAddUser(TokenUtil.getUserName());
            noticeInfo.setAddTime(DateUtils.getNowDate());
            noticeInfo.setDeleted(Constants.DELETE_DEFAULT);
            noticeInfoService.insert(noticeInfo);
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setStatus("fail");
            jsonResult.setMessage(e.getMessage());
        }
        return jsonResult;
    }

    @ApiOperation(value = "通知详情")
    @ResponseBody
    @GetMapping(value = "/detail/{id}")
    public EntityJsonResult noticeInfoDetail(@PathVariable Integer id) {
        EntityJsonResult entityJsonResult = new EntityJsonResult("success","");
        try{
            NoticeInfoKey key = new NoticeInfoKey();
            key.setId(id);
            NoticeInfoWithBLOBs obj = noticeInfoService.selectByPrimaryKey(key);
            entityJsonResult.setData(obj);
        }catch (Exception e){
            e.printStackTrace();
            entityJsonResult.setStatus("fail");
            entityJsonResult.setMessage(e.getMessage());
        }
        return entityJsonResult;
    }

    @ApiOperation(value = "修改通知")
    @ResponseBody
    @PutMapping(value = "/update/{id}")
    public JsonResult updateNoticeInfo(@PathVariable Integer id,@RequestBody NoticeInfoWithBLOBs obj) {
        JsonResult jsonResult = new JsonResult("success","");
        try{
            obj.setId(id);
            obj.setUpdateUser(TokenUtil.getUserName());
            obj.setUpdateTime(DateUtils.getNowDate());
            NoticeInfoExample example = new NoticeInfoExample();
            example.createCriteria().andIdEqualTo(id);
            noticeInfoService.updateByExampleSelective(obj, example);
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setStatus("fail");
            jsonResult.setMessage(e.getMessage());
        }
        return jsonResult;
    }

    @ApiOperation(value = "逻辑删除通知")
    @ResponseBody
    @PutMapping(value = "/delete/{id}")
    public JsonResult deleteNoticeInfo(@PathVariable Integer id) {
        JsonResult jsonResult = new JsonResult("success","");
        try{
            String username = TokenUtil.getUserName();
            NoticeInfoWithBLOBs noticeInfo = new NoticeInfoWithBLOBs();
            noticeInfo.setId(id);
            noticeInfo.setDeleted(Constants.IS_DELETED);
            noticeInfo.setUpdateTime(DateUtils.getNowDate());
            noticeInfo.setUpdateUser(username);
            noticeInfoService.updateByPrimaryKeySelective(noticeInfo);
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setStatus("fail");
            jsonResult.setMessage(e.getMessage());
        }
        return jsonResult;
    }

    @ApiOperation(value = "发布或者撤销通知")
    @ResponseBody
    @PutMapping(value = "/setStatus/{id}")
    public JsonResult setStatus(@PathVariable Integer id,@RequestParam String status) {
        JsonResult jsonResult = new JsonResult("success","");
        try{
            String username = TokenUtil.getUserName();
            NoticeInfoWithBLOBs noticeInfo = new NoticeInfoWithBLOBs();
            noticeInfo.setId(id);
            noticeInfo.setStatus(status);
            noticeInfo.setUpdateTime(DateUtils.getNowDate());
            noticeInfo.setUpdateUser(username);
            noticeInfoService.updateByPrimaryKeySelective(noticeInfo);
        }catch (Exception e){
            e.printStackTrace();
            jsonResult.setStatus("fail");
            jsonResult.setMessage(e.getMessage());
        }
        return jsonResult;
    }
    
}
