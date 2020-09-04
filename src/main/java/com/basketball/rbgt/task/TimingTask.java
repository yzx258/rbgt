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
     * 每天3小时获取比赛，并入库
     */
    @Scheduled(cron = "0 0 0/3 * * ? ")
    private void getStartGame() {
        System.out.println("异步线程开始");
        System.out.println("每天3小时获取比赛");
        taskService.getTodayAllBasketball(DateUtil.getDate(0));
        taskService.getTodayAllBasketball(DateUtil.getDate(1));
        System.out.println("异步线程结束");
    }

    /**
     * 每天1小时获取比赛的结果，并入库
     */
    @Scheduled(cron = "0 0 0/1 * * ? ")
    private void getEndGame() {
        System.out.println("异步线程开始");
        System.out.println("每天2小时获取比赛的结果");
        taskService.getEndBasketballTournament(DateUtil.getDate(0));
        System.out.println("异步线程结束");
    }

    /**
     * 每天2小时获取比赛竞猜结果，并入库
     */
    @Scheduled(cron = "0 0 0/2 * * ? ")
    private void UpdateQuizResult() {
        System.out.println("异步线程开始");
        System.out.println("每天2小时获取比赛竞猜结果，并入库");
        taskService.UpdateByMonthQuizResult();
        System.out.println("异步线程结束");
    }

    /**
     * 每天23小时获取历史报表，并入库
     */
    @Scheduled(cron = "0 0 23 * * ? ")
    private void updateReport() {
        System.out.println("异步线程开始");
        taskService.sendDing();
        System.out.println("异步线程结束");
    }

    /**
     * 每天23小时获取历史报表，并入库
     */
    @Scheduled(cron = "0 0 23 * * ? ")
    private void sendDing() {
        System.out.println("异步线程开始");
        System.out.println("每天2小时获取比赛竞猜结果，并入库");
        taskService.updateReport();
        System.out.println("异步线程结束");
    }

    /**
     * 每天23小时获取历史报表，并入库
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    private void addBet() {
        System.out.println("添加下注指令 - 异步线程开始");
        System.out.println("每1分钟获取比赛竞猜结果，并入库");
        taskService.addBet(DateUtil.getDate(0));
        System.out.println("添加下注指令 - 异步线程结束");
    }
}
