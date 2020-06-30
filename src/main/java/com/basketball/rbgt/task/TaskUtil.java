package com.basketball.rbgt.task;

import com.basketball.rbgt.util.HtmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    private HtmlUtil htmlUtil;

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
    }


    /**
     * 描述：异步获取篮球赛事
     * @param ctime
     */
    @Async("myTaskAsyncPool")
    public void getBasketballTournament(String ctime)
    {
        // 获取篮球赛事
        htmlUtil.allEvent(ctime);
    }

}