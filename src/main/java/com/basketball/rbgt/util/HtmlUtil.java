package com.basketball.rbgt.util;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.basketball.rbgt.mapper.EventMapper;
import com.basketball.rbgt.pojo.Event;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by xuyh at 2017/11/6 14:03.
 * @author yiautos
 */
@Component
public class HtmlUtil {

    @Autowired
    private EventMapper eventMapper;

    /**
     * 描述：获取全部赛事信息
     * @param ctime
     */
    public void allEvent(String ctime,Boolean flag){
        // 爬取的页面信息
        HtmlPage htmlPage = getHtmlPage(ctime);
        // 解析出来的数据对象
        List<Event> event = getEvent(htmlPage,ctime,flag);
        if(flag){
            // 遍历保存数据库
            for(Event e : event){
                QueryWrapper<Event> queryWrapper = new QueryWrapper<Event>();
                queryWrapper.eq("name",e.getName()).eq("start_time",ctime);
                List<Event> events = eventMapper.selectList(queryWrapper);
                if(events.size() == 0)
                {
                    eventMapper.insert(e);
                }else{
                    System.out.println("我是查询出来的对象："+JSON.toJSONString(events));
                }
            }
        }else{
            for(Event e : event) {
                QueryWrapper<Event> queryWrapper = new QueryWrapper<Event>();
                queryWrapper.eq("name",e.getName()).eq("start_time",ctime);
                List<Event> events = eventMapper.selectList(queryWrapper);
                if(events.size() > 0)
                {
                    events.get(0).setPeriodOne(e.getPeriodOne());
                    events.get(0).setPeriodTow(e.getPeriodTow());
                    events.get(0).setPeriodThree(e.getPeriodThree());
                    events.get(0).setPeriodFour(e.getPeriodFour());
                    events.get(0).setStatus(1);
                    eventMapper.updateById(events.get(0));
                }else{
                    System.out.println("我是查询出来的对象："+JSON.toJSONString(events));
                }
            }
        }

    }

    /**
     * 描述：解析赛事 + 获取赛事对象
     * @param htmlPage
     * @param ctime
     * @return
     */
    public List<Event> getEvent(HtmlPage htmlPage,String ctime,Boolean flag)
    {
        // 定义对象
        List<Event> list = new ArrayList<>();
        //直接将加载完成的页面转换成xml格式的字符串
        String pageXml = htmlPage.asXml();
        //获取html文档
        Document document = Jsoup.parse(pageXml);
        int len = document.getElementById("live").getElementsByTag("table").size();
        Calendar now = Calendar.getInstance();
        // 获取当日天数+1,获取明天比赛信息
        String day = ctime.split("-")[2];
        int day_len = day.length();
        if(1 == day_len)
        {
            day = "0"+day;
        }
        String LX, ZD, time, dayBS, dayBSS, KD = "", format = "HH:mm",BSTYPE = "";
        String[] arr = new String[2];
        for (int i = 0; i < len; i++) {
            LX = document.getElementById("live").getElementsByTag("table")
                    .get(i).getElementsByTag("tr").get(0).text();
            if (LX.contains("N") || LX.contains("B") || LX.contains("E")
                    || LX.contains("篮") || LX.contains("甲") || LX.contains("甲")
                    || LX.contains("星") || LX.contains("乙") || LX.contains("女")
                    || LX.contains("友") || LX.contains("东") || LX.contains("西")
                    || LX.contains("联") || LX.contains("杯") || LX.contains("超"))
            {
                BSTYPE = LX;
                i = i + 1;
            }
            time = document.getElementById("live").getElementsByTag("table")
                    .get(i).getElementsByTag("tr").get(0)
                    .getElementsByTag("tr").get(0).getElementsByTag("td")
                    .get(0).text();
            arr = time.split("日");
            dayBS = arr[0];
            try {
                // 判断是否为单日比赛
                if (!(time.contains("完")) && day.equals(dayBS) && flag) {
                    // 获取比赛开始时间
                    dayBSS = arr[1];
                    ZD = document.getElementById("live")
                            .getElementsByTag("table").get(i)
                            .getElementsByTag("tr").get(1)
                            .getElementsByTag("td").get(1).text();
                    KD = document.getElementById("live")
                            .getElementsByTag("table").get(i)
                            .getElementsByTag("tr").get(2)
                            .getElementsByTag("td").get(0).text();
                    Event e = new Event();
                    // 判断比赛是否在规定区间内
                    if (DateUtil.isEffectiveDate(
                            new SimpleDateFormat(format).parse(dayBSS),
                            new SimpleDateFormat(format).parse("10:00"),
                            new SimpleDateFormat(format).parse("20:30"))) {
                        //获取主客队名称
                        //将主客队名称繁体改为简体
                        e.setName(ZD.substring(0, ZD.indexOf("["))+"VS"+KD.substring(0, KD.indexOf("[")));
                        e.setType(getType(BSTYPE));
                        e.setTypeName(BSTYPE);
                        e.setEventTime(time);
                        e.setStartTime(ctime);
                        e.setQuizResults(RandomNumberUtil.getRandomNumber());
                        e.setDeleted(0);
                        e.setVersion("1");
                        e.setCreateTime(new Date());
                        list.add(e);
                    }
                }

                if (time.contains("完") && day.equals(dayBS) && !flag) {
                    //获取主客队名称
                    ZD = document.getElementById("live")
                            .getElementsByTag("table").get(i)
                            .getElementsByTag("tr").get(1)
                            .getElementsByTag("td").get(1).text();
                    KD = document.getElementById("live")
                            .getElementsByTag("table").get(i)
                            .getElementsByTag("tr").get(2)
                            .getElementsByTag("td").get(0).text();
                    Event e = new Event();
                    e.setName(ZD.substring(0, ZD.indexOf("["))+"VS"+KD.substring(0, KD.indexOf("[")));
                    e.setUpdateTime(new Date());
                    e.setPeriodOne(document.getElementById("live")
                            .getElementsByTag("table").get(i)
                            .getElementsByTag("tr").get(1)
                            .getElementsByTag("td").get(2).text()+":"+document.getElementById("live")
                            .getElementsByTag("table").get(i)
                            .getElementsByTag("tr").get(2)
                            .getElementsByTag("td").get(1).text());
                    e.setPeriodTow(document.getElementById("live")
                            .getElementsByTag("table").get(i)
                            .getElementsByTag("tr").get(1)
                            .getElementsByTag("td").get(3).text()+":"+document.getElementById("live")
                            .getElementsByTag("table").get(i)
                            .getElementsByTag("tr").get(2)
                            .getElementsByTag("td").get(2).text());
                    e.setPeriodThree(document.getElementById("live")
                            .getElementsByTag("table").get(i)
                            .getElementsByTag("tr").get(1)
                            .getElementsByTag("td").get(4).text()+":"+document.getElementById("live")
                            .getElementsByTag("table").get(i)
                            .getElementsByTag("tr").get(2)
                            .getElementsByTag("td").get(3).text());
                    e.setPeriodFour(document.getElementById("live")
                            .getElementsByTag("table").get(i)
                            .getElementsByTag("tr").get(1)
                            .getElementsByTag("td").get(5).text()+":"+document.getElementById("live")
                            .getElementsByTag("table").get(i)
                            .getElementsByTag("tr").get(2)
                            .getElementsByTag("td").get(4).text());
                    list.add(e);
                }
                i = i + 1;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e);
                break;
            }
        }
        return list;
    }
    /**
     * 描述：获取对象
     * @return
     */
    public HtmlPage getHtmlPage(String ctime) {
        //新建一个模拟谷歌Chrome浏览器的浏览器客户端对象
        final WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        //当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setActiveXNative(false);
        //是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setCssEnabled(false);
        //很重要，启用JS
        webClient.getOptions().setJavaScriptEnabled(true);
        //很重要，设置支持AJAX
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        HtmlPage hpm = null;
        String url = "";
        try {
            //尝试加载上面图片例子给出的网页
            url = "http://score.nowscore.com/basketball.htm";
            if(StringUtils.isNotEmpty(ctime))
            {
                url = url + "?date=" + ctime;
            }
            HtmlPage page = webClient.getPage(url);
            //异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束
            webClient.waitForBackgroundJavaScript(2000);
            String hrefValue = "javascript:SetLanguage(0);";
            //执行js方法
            ScriptResult s = page.executeJavaScript(hrefValue);
            webClient.waitForBackgroundJavaScript(1000);
            //获得执行后的新page对象
            hpm = (HtmlPage) s.getNewPage();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            webClient.close();
        }
        return hpm;
    }

    /**
     * 根据类型返回type
     * @param typeName
     * @return
     */
    private static int getType(String typeName){
        if("CBA".equals(typeName)){
            return 1;
        }else if("NBA".equals(typeName)){
            return 2;
        }else if("WNBA".equals(typeName)){
            return 3;
        }else if(typeName.contains("以篮")){
            return 4;
        }else if(typeName.contains("纽西联")){
            return 5;
        }else{
            return 6;
        }
    }
}