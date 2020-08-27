package com.basketball.rbgt.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author yiautos
 */
public class DateUtil {
    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 描述：获取时间
     * @param _switch【0：当前时间；1：明天;-1:昨天】
     * @return
     * @throws ParseException
     */
    public static String getDate(int _switch) {
        //1、普通的时间转换
        String string = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //2、日历类的时间操作
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(string));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //增加一天
        if(_switch == 0){
            calendar.add(Calendar.DATE, _switch);
        }else if(_switch == -1){
            calendar.add(Calendar.DATE, _switch);
        }else if(_switch == 1){
            calendar.add(Calendar.DATE, _switch);
        }
        return sdf.format(calendar.getTime()).toString();
    }
}