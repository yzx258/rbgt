package com.basketball.rbgt.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.basketball.rbgt.mapper.EventMapper;
import com.basketball.rbgt.mapper.InstructionMapper;
import com.basketball.rbgt.mapper.RatioMapper;
import com.basketball.rbgt.pojo.Event;
import com.basketball.rbgt.pojo.Instruction;
import com.basketball.rbgt.pojo.Ratio;
import com.basketball.rbgt.service.InstructionService;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by xuyh at 2017/11/6 14:03.
 * @author yiautos
 */
@Component
@Slf4j
public class HtmlUtil {

    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private RatioMapper ratioMapper;
    @Autowired
    private InstructionService instructionService;
    @Autowired
    private InstructionMapper instructionMapper;

    /**
     * 描述：比赛队伍名过滤条件
     */
    private static final String KL = "[";

    /**
     * 描述：比赛类型
     */
    private static final String CBA = "CBA";
    private static final String NBA = "NBA";
    private static final String W_NBA = "WNBA";
    private static final String YL = "以篮";
    private static final String NXL = "以纽西联篮";

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
            insertEvent(event,ctime);
        }else{
            // 批量插入数据
            updateEvent(event,ctime);
        }
        // 判断是否是当前时间
        System.out.println(ctime.equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date())+""));
        if(ctime.equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) && flag){
            HtmlPage hps = getHtmlPage(DateUtil.getDate(1));
            List<Event> el = getTomorrowEvent(hps,DateUtil.getDate(0),flag);
            // 批量插入数据
            insertEvent(el,ctime);
        }
    }

    /**
     * 描述：新增下注指令【第一节 - 第四节】
     * @param ctime
     */
    public void allBetEvent(String ctime){
        // 爬取的页面信息
        HtmlPage htmlPage = getHtmlPage(ctime);
        // 解析出来的数据对象
        List<Event> event = getBetEvent(htmlPage,ctime);
        // 过滤数据【只包含：CBA和NBA赛事】
        List<Event> collect = event.stream().filter(e -> (e.getType() == 1 || e.getType() == 2)).collect(Collectors.toList());
        // 判断每节是否红单
        for(Event e : collect) {
            QueryWrapper<Event> queryWrapper = new QueryWrapper<Event>();
            queryWrapper.eq("name",e.getName()).eq("start_time",e.getStartTime());
            List<Event> es = eventMapper.selectList(queryWrapper);
            log.info("获取的比赛 - > {}",JSON.toJSONString(e));
            // 判断比赛是否开始
            if(":".equals(e.getPeriodOne())){
                log.info("该比赛未开始 -> {},{},{}",es.get(0).getName(),es.get(0).getResults(),es.get(0).getStartTime());
                continue;
            }
            // 判断比赛是否红单
            if(StringUtils.isNotEmpty(es.get(0).getResults())){
                log.info("该比赛已出比赛结果 -> {},{},{}",es.get(0).getName(),es.get(0).getResults(),es.get(0).getStartTime());
                continue;
            }
            // 判断是否有数据
            if(es.size() == 1)
            {
                Event event1 = es.get(0);
                event1.setName(e.getName().replace("顿","敦"));
                // 判断支付指令是否已红单
                if(instructionService.checkInstructionRed(event1,e)){
                    log.info("该比赛已红单 -> {},{}",event1.getName(),event1.getStartTime());
                    continue;
                }
                // 下注场次【默认为1】
                int betSession = 1;
                String instructionId = null;
                // 判断第一节
                if(StringUtils.isNotEmpty(e.getPeriodOne()) && ":".equals(e.getPeriodTow())){
                    // 判断是否已购买
                    QueryWrapper<Instruction> qw = new QueryWrapper<Instruction>();
                    qw.eq("bet_htn",event1.getName().split("VS")[0]).eq("bet_time",DateUtil.getDate(0)).eq("bet_session",1);
                    List<Instruction> is = instructionMapper.selectList(qw);
                    // 第一节,新增支付指令
                    if(is.size() == 1){
                        log.info("第一节已购买，等待比赛结束 -> {},{}",event1.getName(),event1.getStartTime());
                        continue;
                    }
                    // 判断是否存在黑五场的数据
                    QueryWrapper<Instruction> black = new QueryWrapper<Instruction>();
                    black.eq("bet_status",5);
                    List<Instruction> blackList = instructionMapper.selectList(black);
                    if(blackList.size() > 0){
                        log.info("存在黑五场的数据 - > {},{}",blackList.get(0).getBetAtn()+"VS"+blackList.get(0).getBetHtn(),blackList.get(0).getBetTime());
                        betSession = 5;
                        instructionId = blackList.get(0).getId();
                    }
                    else{
                        betSession = 1;
                    }
                    instructionService.add(es.get(0),e,betSession,instructionId);
                    continue;
                }
                else if(!":".equals(e.getPeriodTow()) && ":".equals(e.getPeriodThree())){
                    // 判断第一节是否红了
                    if(instructionService.checkInstruction(event1,e,1)){
                        log.info("第一节已红单 -> {},{}",event1.getName(),event1.getStartTime());
                        continue;
                    }
                    // 判断是否已购买
                    QueryWrapper<Instruction> qw = new QueryWrapper<Instruction>();
                    qw.eq("bet_htn",event1.getName().split("VS")[0]).eq("bet_time",DateUtil.getDate(0)).eq("bet_session",2);
                    List<Instruction> is = instructionMapper.selectList(qw);
                    if(is.size() == 1){
                        log.info("第二节已购买，等待比赛结束 -> {},{}",event1.getName(),event1.getStartTime());
                        continue;
                    }
                    QueryWrapper<Instruction> qw1 = new QueryWrapper<Instruction>();
                    qw1.eq("bet_htn",event1.getName().split("VS")[0]).eq("bet_time",DateUtil.getDate(0)).eq("bet_session",1).eq("bet_status",4);
                    List<Instruction> is1 = instructionMapper.selectList(qw1);
                    if(is1.size() == 1 && StringUtils.isNotEmpty(is1.get(0).getInstructionId())){
                        betSession = 6;
                        instructionId = is1.get(0).getInstructionId();
                    }else{
                        betSession = 2;
                    }
                    // 第二节
                    instructionService.add(es.get(0),e,betSession,instructionId);
                    continue;
                }else if(!":".equals(e.getPeriodThree()) && ":".equals(e.getPeriodFour())){
                    // 判断第二节是否红了
                    if(instructionService.checkInstruction(event1,e,2)){
                        log.info("第二节已红单 -> {},{}",event1.getName(),event1.getStartTime());
                        continue;
                    }
                    // 判断是否已购买
                    QueryWrapper<Instruction> qw = new QueryWrapper<Instruction>();
                    qw.eq("bet_htn",event1.getName().split("VS")[0]).eq("bet_time",DateUtil.getDate(0)).eq("bet_session",3);
                    List<Instruction> is = instructionMapper.selectList(qw);
                    if(is.size() == 1){
                        log.info("第三节已购买，等待比赛结束 -> {},{}",event1.getName(),event1.getStartTime());
                        continue;
                    }
                    QueryWrapper<Instruction> qw1 = new QueryWrapper<Instruction>();
                    qw1.eq("bet_htn",event1.getName().split("VS")[0]).eq("bet_time",DateUtil.getDate(0)).eq("bet_session",2).eq("bet_status",4);
                    List<Instruction> is1 = instructionMapper.selectList(qw1);
                    if(is1.size() == 1 && StringUtils.isNotEmpty(is1.get(0).getInstructionId())){
                        betSession = 7;
                        instructionId = is1.get(0).getInstructionId();
                    }else{
                        betSession = 3;
                    }
                    // 第三节
                    instructionService.add(es.get(0),e,betSession,instructionId);
                    continue;
                }else if(!":".equals(e.getPeriodFour()) && "比赛进行中".equals(e.getOverTimeFive())){

                    // 判断第三节是否红了
                    if(instructionService.checkInstruction(event1,e,3)){
                        log.info("第三节已红单 -> {},{}",event1.getName(),event1.getStartTime());
                        continue;
                    }
                    // 判断是否已购买
                    QueryWrapper<Instruction> qw = new QueryWrapper<Instruction>();
                    qw.eq("bet_htn",event1.getName().split("VS")[0]).eq("bet_time",DateUtil.getDate(0)).eq("bet_session",4);
                    List<Instruction> is = instructionMapper.selectList(qw);
                    if(is.size() == 1){
                        log.info("第四节已购买，等待比赛结束 -> {},{}",event1.getName(),event1.getStartTime());
                        continue;
                    }
                    QueryWrapper<Instruction> qw1 = new QueryWrapper<Instruction>();
                    qw1.eq("bet_htn",event1.getName().split("VS")[0]).eq("bet_time",DateUtil.getDate(0)).eq("bet_session",3).eq("bet_status",4);
                    List<Instruction> is1 = instructionMapper.selectList(qw1);
                    if(is1.size() == 1 && StringUtils.isNotEmpty(is1.get(0).getInstructionId())){
                        betSession = 8;
                        instructionId = is1.get(0).getInstructionId();
                    }else{
                        betSession = 4;
                    }
                    // 新增第四节下注指令
                    instructionService.add(es.get(0),e,betSession,instructionId);
                    continue;
                }else if(!":".equals(e.getPeriodFour()) && "比赛结束".equals(e.getOverTimeFive())){
                    // 第四节判断，是否全黑
                    int k1,k2 = 0;
                    String[] splitf = e.getPeriodFour().split(":");
                    k1 = Integer.parseInt(splitf[0]);
                    k2 = Integer.parseInt(splitf[1]);
                    String[] splitb = event1.getQuizResults().split(",");
                    String ds = "双";
                    if((k1+k2)%2==1){
                        ds = "单";
                    }
                    QueryWrapper<Instruction> qw = new QueryWrapper<Instruction>();
                    qw.eq("bet_htn",event1.getName().split("VS")[0]).eq("bet_time",DateUtil.getDate(0)).eq("bet_status",2);
                    List<Instruction> is = instructionMapper.selectList(qw);
                    if(is.size() == 1){
                        Instruction instruction = is.get(0);
                        if(ds.equals(splitb[3])){
                            // 更新第四节的下注指令
                            if(instruction.getBetSession() == 8){
                                // 将4黑数据清空一笔
                                QueryWrapper<Instruction> black = new QueryWrapper<Instruction>();
                                black.eq("bet_status",5);
                                List<Instruction> blackList = instructionMapper.selectList(black);
                                if(blackList.size() > 0){
                                    Instruction instruction1 = blackList.get(0);
                                    instruction1.setBetStatus(4);
                                    instructionMapper.updateById(instruction1);
                                }
                            }
                            instruction.setBetStatus(3);
                            instructionMapper.updateById(instruction);
                            continue;
                        }else{
                            // 更新第四节的下注指令
                            instruction.setBetStatus(5);
                            instructionMapper.updateById(instruction);
                        }
                    }
                }
            }
        }
    }

    /**
     * 描述：获取单日全部赛事信息
     * @param ctime
     */
    public void insertAllEvent(String ctime){
        // 爬取的页面信息
        HtmlPage htmlPage = getHtmlPage(ctime);
        // 解析出来的数据对象
        List<Event> event = getAllEvent(htmlPage,ctime);
        // 遍历保存数据库
        insertEvent(event,ctime);
    }



    /**
     * 描述：批量插入数据
     * @param list
     * @param ctime
     */
    public void insertEvent(List<Event> list,String ctime)
    {
        for(Event e : list) {
            QueryWrapper<Event> queryWrapper = new QueryWrapper<Event>();
            queryWrapper.eq("name",e.getName()).eq("start_time",e.getStartTime());
            List<Event> es = eventMapper.selectList(queryWrapper);
            if(es.size() == 0)
            {
                eventMapper.insert(e);
            }else{
                System.out.println("我是查询出来的对象："+JSON.toJSONString(es));
            }
        }
    }

    /**
     * 描述：批量插入数据
     * @param list
     * @param ctime
     */
    public void updateEvent(List<Event> list,String ctime)
    {
        for(Event e : list){
            QueryWrapper<Event> queryWrapper = new QueryWrapper<Event>();
            queryWrapper.eq("name",e.getName()).eq("start_time",ctime);
            List<Event> es = eventMapper.selectList(queryWrapper);
            if(es.size() > 0)
            {
                es.get(0).setPeriodOne(e.getPeriodOne());
                es.get(0).setPeriodTow(e.getPeriodTow());
                es.get(0).setPeriodThree(e.getPeriodThree());
                es.get(0).setPeriodFour(e.getPeriodFour());
                es.get(0).setStatus(1);
                eventMapper.updateById(es.get(0));
            }else{
                System.out.println("我是查询出来的对象："+JSON.toJSONString(es));
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
                            new SimpleDateFormat(format).parse("06:00"),
                            new SimpleDateFormat(format).parse("23:59"))) {
                        //获取主客队名称
                        //将主客队名称繁体改为简体
                        e.setName(getEventName(ZD,KD));
                        e.setType(getType(BSTYPE));
                        e.setTypeName(BSTYPE);
                        e.setEventTime(time);
                        e.setStartTime(ctime);
                        e.setQuizResults(RandomNumberUtil.getRandomNumber());
                        e.setDeleted(0);
                        e.setVersion("1");
                        e.setCreateTime(new Date());
                        e.setYear(ctime.split("-")[0]);
                        e.setMonth(ctime.split("-")[1]);
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
                    e.setName(getEventName(ZD,KD));
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
     * 描述：解析赛事 + 获取赛事对象
     * @param htmlPage
     * @param ctime
     * @return
     */
    public List<Event> getBetEvent(HtmlPage htmlPage,String ctime)
    {
        // 定义对象
        List<Event> list = new ArrayList<>();
        //直接将加载完成的页面转换成xml格式的字符串
        String pageXml = htmlPage.asXml();
        //获取html文档
        Document document = Jsoup.parse(pageXml);
        int len = document.getElementById("live").getElementsByTag("table").size();
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
                if (day.equals(dayBS)) {
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
                    e.setName(getEventName(ZD,KD));
                    e.setType(getType(BSTYPE));
                    e.setStartTime(DateUtil.getDate(0));
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
                    if((time.contains("完"))){
                        e.setOverTimeFive("比赛结束");
                    }else{
                        e.setOverTimeFive("比赛进行中");
                    }
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
     * 描述：解析赛事 + 获取赛事对象
     * @param htmlPage
     * @param ctime
     * @return
     */
    public List<Event> getAllEvent(HtmlPage htmlPage,String ctime)
    {
        // 定义对象
        List<Event> list = new ArrayList<>();
        //直接将加载完成的页面转换成xml格式的字符串
        String pageXml = htmlPage.asXml();
        //获取html文档
        Document document = Jsoup.parse(pageXml);
        int len = document.getElementById("live").getElementsByTag("table").size();
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
                if (!(time.contains("完"))) {
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
                            new SimpleDateFormat(format).parse("00:01"),
                            new SimpleDateFormat(format).parse("23:59"))) {
                        //获取主客队名称
                        //将主客队名称繁体改为简体
                        e.setName(getEventName(ZD,KD));
                        e.setType(getType(BSTYPE));
                        e.setTypeName(BSTYPE);
                        e.setEventTime(time);
                        e.setStartTime(ctime.split("-")[0]+"-"+ctime.split("-")[1]+"-"+dayBS);
                        e.setQuizResults(RandomNumberUtil.getRandomNumber());
                        e.setDeleted(0);
                        e.setVersion("1");
                        e.setCreateTime(new Date());
                        e.setYear(ctime.split("-")[0]);
                        e.setMonth(ctime.split("-")[1]);
                        list.add(e);
                    }
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
     * 描述：解析赛事 + 获取赛事对象
     * @param htmlPage
     * @param ctime
     * @return
     */
    public List<Event> getTomorrowEvent(HtmlPage htmlPage,String ctime,Boolean flag)
    {
        // 定义对象
        List<Event> list = new ArrayList<>();
        //直接将加载完成的页面转换成xml格式的字符串
        String pageXml = htmlPage.asXml();
        //获取html文档
        Document document = Jsoup.parse(pageXml);
        int len = document.getElementById("live").getElementsByTag("table").size();
        // 获取当日天数+1,获取明天比赛信息
        String day = DateUtil.getDate(0).split("-")[2];
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
                System.out.println(day.equals(dayBS));
                System.out.println(flag);
                if (day.equals(dayBS) && flag) {
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
                            new SimpleDateFormat(format).parse("08:00"),
                            new SimpleDateFormat(format).parse("23:59"))) {
                        //获取主客队名称
                        e.setName(getEventName(ZD,KD));
                        e.setType(getType(BSTYPE));
                        e.setTypeName(BSTYPE);
                        e.setEventTime(time);
                        e.setStartTime(ctime);
                        e.setQuizResults(RandomNumberUtil.getRandomNumber());
                        e.setDeleted(0);
                        e.setVersion("1");
                        e.setCreateTime(new Date());
                        e.setYear(ctime.split("-")[0]);
                        e.setMonth(ctime.split("-")[1]);
                        list.add(e);
                    }
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
     * 描述：获取队伍名称
     * @param zd
     * @param kd
     * @return
     */
    public String getEventName(String zd,String kd)
    {
        if(zd.contains(KL)){
            zd = zd.substring(0, zd.indexOf(KL));
        }
        if(kd.contains(KL)){
            kd = kd.substring(0, kd.indexOf(KL));
        }
        return zd+"VS"+kd;
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
    public int getType(String typeName){
        if(CBA.equals(typeName) || "CBA季后赛".equals(typeName) || "CBA季后".equals(typeName)){
            return 1;
        }else if(NBA.equals(typeName) || "NBA季后赛".equals(typeName) || "NBA季后".equals(typeName)){
            return 2;
        }else if(W_NBA.equals(typeName)){
            return 3;
        }else if(typeName.contains(YL)){
            return 4;
        }else if(typeName.contains(NXL)){
            return 5;
        }else{
            return 6;
        }
    }
}