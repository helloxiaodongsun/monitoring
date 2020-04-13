package com.pactera.monitoring.quartz;

import com.pactera.monitoring.quartz.service.QuartzJobService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**定时任务执行类
 * @author 8
 */
@Component
@Slf4j
@Async
public class QuartzJob {


    @Autowired
    QuartzJobService quartzJobService;

    //@Scheduled(cron = "0/20 * * * * ?")
    public void saveAndSelectFromServer() {
        try {
            quartzJobService.getInformationAndSave();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
