package com.basketball.rbgt.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

/**
 * @program: rbgt
 * @author: 俞春旺
 * @create: 2020/06/14 09:03
 * @description： 描述：
 */
@Service
public class TaskUtil {

    @Autowired
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Async("myTaskAsyncPool")
    public void test() {
        for(int i = 0;i<10;i++){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("我被异步调用了");
        }
        //判断线程是否全部执行结束
        while (threadPoolTaskExecutor.getActiveCount() > 0){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("执行结束");

    }

}