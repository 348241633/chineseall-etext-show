package com.common.quartz.task;

import com.edusoft.sysmanage.service.DataSyncService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * Created by lego-jspx01 on 2018/7/30.
 */
@Component("syncDictTask")
public class SyncDictTask{
    private static final Log log = LogFactory.getLog(SyncDictTask.class);
    @Resource
    private DataSyncService dataSyncService;
    public void task() {
        log.info("======start===dict=========");
        try {
            dataSyncService.syncDictInfo();
        }catch (Exception e){
            e.printStackTrace();
            log.error("======sync===dict========error:"+e.getMessage());
        }
        log.info("======end===dict=========");
    }
}
