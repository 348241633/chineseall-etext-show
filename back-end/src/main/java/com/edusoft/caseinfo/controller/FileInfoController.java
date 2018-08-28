package com.edusoft.caseinfo.controller;

import com.common.security.TokenUtil;
import com.common.util.Constants;
import com.common.util.DateUtils;
import com.common.util.ObsClientUtil;
import com.common.util.StorageUtil;
import com.edusoft.caseinfo.model.CaseDetailFile;
import com.edusoft.caseinfo.model.CaseDetailFileKey;
import com.edusoft.caseinfo.service.CaseFileService;
import com.edusoft.caseinfo.service.TeachingCaseService;
import com.edusoft.sysmanage.service.DictionaryInfoService;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lego-jspx01 on 2018/6/9.
 */
@Api(value = "系统文件管理", description = "文件管理API",tags = "FileResourceApi", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping("/caseFile")
public class FileInfoController {
    private static final Log log = LogFactory.getLog(FileInfoController.class);
    private static final String dict_flag = "t_recource_type,t_xueke";

    @Autowired
    TeachingCaseService teachingCaseService;
    @Autowired
    DictionaryInfoService dictionaryInfoService;
    @Resource
    private CaseFileService caseFileService;
    @Value("${azure.container.name}")
    private String containerName;

    @ApiOperation(value = "文件下载")
    @ResponseBody
    @GetMapping(value = "/download/{id}")
    public void downloadResource(@PathVariable Integer id,HttpServletResponse response) {
        try {
            CaseDetailFileKey  key = new CaseDetailFileKey();
            key.setId(id);
            CaseDetailFile resourceInfo = caseFileService.selectByPrimaryKey(key);
            String objectKey = resourceInfo.getFilePath();
            if("".equals(objectKey)) return ;
            String filename = resourceInfo.getFileName();
            response.setContentType("multipart/form-data");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO-8859-1"));
            //------------------------
            OutputStream out = response.getOutputStream();
            CloudBlobContainer container = StorageUtil.getBlobContainer(containerName);
            StorageUtil.downloadObsClientFile(container,objectKey,out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "文件下载")
    @ResponseBody
    @GetMapping(value = "/previewdownload/{id}")
    public void downloadResourcePerview(@PathVariable Integer id,HttpServletResponse response) {
        try {
            CaseDetailFileKey key = new CaseDetailFileKey();
            key.setId(id);
            CaseDetailFile caseDetailFile = caseFileService.selectByPrimaryKey(key);
            String objectKey = caseDetailFile.getFilePath();
            if("".equals(objectKey)) return ;
            String filename = caseDetailFile.getFileName();
            response.setContentType("multipart/form-data");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO-8859-1"));
            OutputStream out = response.getOutputStream();
            InputStream inputStream = ObsClientUtil.downloadObsClientFile(containerName, objectKey);
            byte[] buffer = new byte[2048];
            int i = -1;
            while ((i = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, i);
            }
            out.flush();
            out.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "图片下载")
    @ResponseBody
    @GetMapping(value = "/picture/{id}")
    public void downloadPicResource(@PathVariable Integer id,HttpServletResponse response) {
        try {
            CaseDetailFileKey key = new CaseDetailFileKey();
            key.setId(id);
            CaseDetailFile caseDetailFile = caseFileService.selectByPrimaryKey(key);
            String objectKey = caseDetailFile.getFilePath();
            String fileName = objectKey.substring(objectKey.lastIndexOf("/")+1);
            response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("utf-8"), "iso8859-1"));
            OutputStream out = response.getOutputStream();
            InputStream inputStream = ObsClientUtil.downloadObsClientFile(containerName,objectKey);
            byte[] buffer = new byte[2048];
            int i = -1;
            while ((i = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, i);
            }
            out.flush();
            out.close();
            inputStream.close();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "教学案例文件分模块上传")
    @ResponseBody
    @PostMapping(value = "/upload/{id}/{modtype}")
    public Map uploadResource(@PathVariable Integer id,@PathVariable String modtype,@RequestParam("upfile_attachment")MultipartFile file,HttpServletRequest request )throws Exception{
        String newFileName = DateUtils.getIndexDateTime();
        Map<String,Object> map = new HashMap<String,Object>();
        File localFile = null;
        try {
            String username = TokenUtil.getUserName();
            if(null!=username){
                if(!file.isEmpty()) {
                    String fileName = file.getOriginalFilename();
                    String s3filepath = modtype + "/"+ newFileName+ "-" +username +"/"+fileName;
                    String basicPath = request.getServletContext().getRealPath("/tmp");//临时文件根目录
                    String localPath = basicPath +username +newFileName +fileName;
                    localFile = new File(localPath);
                    file.transferTo(localFile);
                    CloudBlobContainer container = StorageUtil.getBlobContainer(containerName);
                    StorageUtil.uploadFileToStorage(container, s3filepath, localFile, localFile.length());
                    CaseDetailFile obj = new CaseDetailFile();
                    obj.setDeleted(Constants.DELETE_DEFAULT);
                    obj.setFilePath(s3filepath);
                    obj.setCaseId(id);
                    obj.setCaseType(modtype);
                    obj.setFileName(file.getName());
                    obj.setFileType(file.getContentType());
                    obj.setFileSize(file.getSize()+"");
                    caseFileService.insert(obj);
                }
            }else{
                map.put("status","fail");
                map.put("message","session过期上传文件失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
            map.put("status","fail");
            map.put("message","上传文件失败");
        }finally {
            if(null!=localFile)
                localFile.delete();
        }
        return map;
    }


//    String[] titles = {"subject","title","brief","classify","resourceType","languages","suitUserType",
//                      "teachCode","courseCode","grade","term","unitIndex","unitName",
//    "chapterIndex","chapterName","keyWords","applyArea","visitAuthority", "oldUrl","bigPicUrl"};
//    List<ResourceInfoVO> list = (List<ResourceInfoVO>) readExcel.getDataList(file.getInputStream(),ResourceInfoVO.class,titles,1);

    /**
     * 导入教学案列信息
     * @param requestMap
     * @return
     */
    @ApiOperation(value = "案列信息导入")
    @ResponseBody
    @PostMapping(value = "/importCaseInfo")
    public Map importmember(@RequestBody Map<String,List<?>> requestMap ) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
//        try {
//            List<ResourceInfoVO> list = requestMap.get("data");
//            if(null != list && list.size() > 0) {
//                log.debug("===========================================" + list.size());
//                Map<String, String> dictMap = dictionaryInfoService.selectAllSubDictByName(dict_flag);
//                //校验重复??
//                String username = TokenUtil.getUserName();
//                String newFileName = DateUtils.getIndexDateTime();
//                String obsPath = "import/"+ newFileName+ "-" +username +"/";
//                for(ResourceInfoVO obj:list){
//                    obj = this.checkResourceInfo(obj,dictMap,username);
//                    if(!StringUtils.isEmpty(obj.getImportMsg()))
//                        continue;
//                    resourceInfoService.importResouces(obsPath,obj);
//                }
//                resultMap.put("data",list);
//                log.debug("===========================================0");
//            }else {
//                resultMap.put("message", "导入文件为空");
//                resultMap.put("status","fail");
//            }
//        }catch (Exception e){
//            log.error(e.getClass());
//            e.printStackTrace();
//            resultMap.put("status","fail");
//            resultMap.put("message","批量导入数据异常");
//            resultMap.put("data","批量导入失败");
//        }
        return resultMap;
    }


}
