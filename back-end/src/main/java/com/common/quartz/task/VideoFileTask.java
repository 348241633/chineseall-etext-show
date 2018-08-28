package com.common.quartz.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * Created by lego-jspx01 on 2018/7/30.
 */
@Component
public class VideoFileTask {
    private static final Log log = LogFactory.getLog(VideoFileTask.class);

    public void task(){
        //查询出未转码的视频
//        ResourceFileExample example = new ResourceFileExample();
//        List<String> uploadFlag = new ArrayList<>();
//        uploadFlag.add(Constants.NO_UPLOAD_PIC);
//        uploadFlag.add(Constants.UPLOAD_FLAG);
//        example.createCriteria().andDealFlagEqualTo(Constants.VIDEO_DEAL_STATUS)
//                .andUploadFlagIn(uploadFlag);
//        List<ResourceFile> fileList = resourceFileService.selectByExample(example);//查询所有需要转换的文档
//        //转码处理
//        if(null != fileList && fileList.size()>0){
//            MpcClient mpcClient = MpcUtil.initConfig();
//            for(ResourceFile obj : fileList){
//                String filePath = obj.getFilePath();
//                filePath = filePath.substring(0,filePath.lastIndexOf("."))+ "/" + DateUtils.getIndexDateTime()+".mp4";
//                MpcUtil.macTask(mpcClient,"private-test-file",obj.getFilePath(),filePath);
//            }
//        }

        //更新状态
        System.out.println("========================VideoFileTask==========================");
    }
}
