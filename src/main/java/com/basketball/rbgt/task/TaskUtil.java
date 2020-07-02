package com.basketball.rbgt.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.basketball.rbgt.mapper.EventMapper;
import com.basketball.rbgt.pojo.Event;
import com.basketball.rbgt.util.HtmlUtil;
import com.basketball.rbgt.util.QuizResultsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    private QuizResultsUtil quizResultsUtil;
    @Autowired
    private EventMapper eventMapper;

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
        htmlUtil.allEvent(ctime,true);
    }

    /**
     * 描述：异步获取篮球赛事
     * @param ctime
     */
    @Async("myTaskAsyncPool")
    public void getEndBasketballTournament(String ctime)
    {
        // 获取篮球赛事
        htmlUtil.allEvent(ctime,false);
    }

    /**
     * 描述：异步获取篮球赛事
     * @param ctime
     */
    @Async("myTaskAsyncPool")
    public void UpdateQuizResult(String ctime)
    {
        QueryWrapper<Event> queryWrapper = new QueryWrapper<Event>();
        queryWrapper.eq("start_time",ctime).eq("status",1);
        List<Event> events = eventMapper.selectList(queryWrapper);
        for (Event e : events){
            e.setResults(quizResultsUtil.getQuizResultsUtil(e));
            e.setStatus(2);
            eventMapper.updateById(e);
        }
    }

}