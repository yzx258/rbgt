package com.basketball.rbgt.util;

import com.basketball.rbgt.pojo.Event;
import org.springframework.stereotype.Component;

/**
 * 描述：竞猜结果工具类
 * @author yucw
 * @date 2020/7/2 10:37
 */
@Component
public class QuizResultsUtil {

    /**
     * 描述：根据对象获取竞猜结果
     * @param e
     * @return
     */
    public String getQuizResultsUtil(Event e)
    {
        // 获取竞猜结果
        String[] split = e.getQuizResults().split(",");
        // 获取第一节比分
        String[] split1 = e.getPeriodOne().split(":");
        // 获取第二节比分
        String[] split2 = e.getPeriodTow().split(":");
        // 获取第三节比分
        String[] split3 = e.getPeriodThree().split(":");
        // 获取第四届比分
        String[] split4 = e.getPeriodFour().split(":");
        String results = "黑",quiz = "";
        int zd=0,kd=0;
        zd = Integer.parseInt(split1[0]);
        kd = Integer.parseInt(split1[1]);
        // 第一节判断
        if((zd+kd)%2 == 1){
            quiz = "单";
        }else{
            quiz = "双";
        }
        if(split[0].equals(quiz)){
            results = "红";
            return results;
        }
        // 第二节判断
        zd = Integer.parseInt(split2[0]);
        kd = Integer.parseInt(split2[1]);
        if((zd+kd)%2 == 1){
            quiz = "单";
        }else{
            quiz = "双";
        }
        if(split[1].equals(quiz)){
            results = "红";
            return results;
        }
        // 第三节判断
        zd = Integer.parseInt(split3[0]);
        kd = Integer.parseInt(split3[1]);
        if((zd+kd)%2 == 1){
            quiz = "单";
        }else{
            quiz = "双";
        }
        if(split[2].equals(quiz)){
            results = "红";
            return results;
        }
        // 第四节判断
        zd = Integer.parseInt(split4[0]);
        kd = Integer.parseInt(split4[1]);
        if((zd+kd)%2 == 1){
            quiz = "单";
        }else{
            quiz = "双";
        }
        if(split[3].equals(quiz)){
            results = "红";
            return results;
        }
        return results;
    }
}
