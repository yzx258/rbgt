package com.basketball.rbgt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.basketball.rbgt.mapper.InstructionMapper;
import com.basketball.rbgt.mapper.RatioMapper;
import com.basketball.rbgt.pojo.Event;
import com.basketball.rbgt.pojo.Instruction;
import com.basketball.rbgt.pojo.Ratio;
import com.basketball.rbgt.service.InstructionService;
import com.basketball.rbgt.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 俞春旺
 */
@Service
@Slf4j
public class InstructionServiceImpl implements InstructionService {

    @Autowired
    private InstructionMapper instructionMapper;
    @Autowired
    private RatioMapper ratioMapper;


    /**
     * 新增下注指令
     * @param event1
     * @param e
     * @param betSession
     */
    @Override
    public void add(Event event1, Event e,Integer betSession) {
        // 第一节
        Ratio ratio = ratioMapper.selectById("1288129806892171266");
        JSONArray objects = JSON.parseArray(ratio.getBetRange());
        System.out.println("倍率：" + ratio.getBetRange());
        int k1, k2 = 0;
        String[] split = e.getPeriodOne().split(":");
        k1 = Integer.parseInt(split[0]);
        k2 = Integer.parseInt(split[1]);
        if ((k1 + k2) > 0) {
            // 正在比赛，插入比赛指令
            Instruction instruction = new Instruction();
            System.out.println(objects.get(0) + "");
            instruction.setBetAmount(Integer.parseInt(objects.get(betSession-1) + ""));
            instruction.setBetHtn(event1.getName().split("VS")[0]);
            instruction.setBetAtn(event1.getName().split("VS")[1]);
            instruction.setBetSession(betSession);
            if(1 == betSession || 5 == betSession){
                instruction.setBetSessionName("总得分:滚球 单 / 双-第一节");
            }else if(2 == betSession || 6 == betSession){
                instruction.setBetSessionName("总得分:滚球 单 / 双-第二节");
            }else if(3 == betSession || 7 == betSession){
                instruction.setBetSessionName("总得分:滚球 单 / 双-第三节");
            }else if(4 == betSession || 8 == betSession){
                instruction.setBetSessionName("总得分:滚球 单 / 双");
            }
            String[] split1 = event1.getQuizResults().split(",");
            if ("单".equals(split1[0])) {
                instruction.setBetSingleOrDouble(1);
            } else {
                instruction.setBetSingleOrDouble(2);
            }
            instruction.setCreateTime(new Date());
            instruction.setBetStatus(2);
            instruction.setBetNumber(0);
            instruction.setBetSession(betSession >= 5 ? betSession - 4 : betSession);
            instruction.setBetTime(DateUtil.getDate(0));
            instructionMapper.insert(instruction);
            log.info("插入第"+betSession+"节下注指令 - > {},{}", e.getName(), e.getStartTime());
        }
    }

    /**
     * 校验支付指令是否红单
     * @param event1
     * @param e
     * @return
     */
    @Override
    public Boolean checkInstruction(Event event1, Event e,Integer betSession) {
        int k1,k2 =0;
        String[] splitf = null;
        if(betSession == 1 || betSession == 5 ){
            splitf = e.getPeriodOne().split(":");
        }else if(betSession == 2 || betSession == 6 ){
            splitf = e.getPeriodTow().split(":");
        }else if(betSession == 3 || betSession == 7 ){
            splitf = e.getPeriodThree().split(":");
        }else if(betSession == 4 || betSession == 8 ){
            splitf = e.getPeriodFour().split(":");
        }
        k1 = Integer.parseInt(splitf[0]);
        k2 = Integer.parseInt(splitf[1]);
        String[] splitb = event1.getQuizResults().split(",");
        String ds = "双";
        if((k1+k2)%2==1){
            ds = "单";
        }
        QueryWrapper<Instruction> qw = new QueryWrapper<Instruction>();
        qw.eq("bet_htn",event1.getName().split("VS")[0]).eq("bet_time",DateUtil.getDate(0)).eq("bet_status",2).eq("bet_session",betSession);
        List<Instruction> is = instructionMapper.selectList(qw);
        if(is.size() == 1){
            Instruction instruction = is.get(0);
            if(ds.equals(splitb[betSession >= 5 ? betSession-4 : betSession])){
                // 更新第一节的下注指令
                instruction.setBetStatus(3);
                instructionMapper.updateById(instruction);
                return true;
            }else{
                // 更新第一节的下注指令
                instruction.setBetStatus(4);
                instructionMapper.updateById(instruction);
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * 校验支付指令是否已经存在红单
     * @param event1
     * @param e
     * @return
     */
    @Override
    public Boolean checkInstructionRed(Event event1, Event e) {
        QueryWrapper<Instruction> qw = new QueryWrapper<Instruction>();
        qw.eq("bet_htn",event1.getName().split("VS")[0]).eq("bet_time",DateUtil.getDate(0)).eq("bet_status",3);
        List<Instruction> is = instructionMapper.selectList(qw);
        if(is.size() >= 1){
            return true;
        }
        return false;
    }

    /**
     * 根据状态和时间获取下注指令
     * @return
     */
    @Override
    public List<Instruction> getByStatus() {
        QueryWrapper<Instruction> qw = new QueryWrapper<Instruction>();
        qw.eq("bet_time",DateUtil.getDate(0)).eq("bet_status",1);
        List<Instruction> is = instructionMapper.selectList(qw);
        return is;
    }
}
