package com.basketball.rbgt.task;

import com.basketball.rbgt.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 俞春旺
 */
@Component
@Configuration
@EnableScheduling
public class TimingTask {

    @Autowired
    private TaskUtil taskService;

    /**
     * 每天13点40获取比赛的结果，并入库
     */
    @Scheduled(cron = "0 50 23 * * ?")
    private void TaskTow() {
        System.out.println("异步线程开始");
        taskService.getBasketballTournament(DateUtil.getDate(1));
        System.out.println("异步线程结束");
    }
}
