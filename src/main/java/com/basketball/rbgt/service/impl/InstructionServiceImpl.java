package com.basketball.rbgt.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.basketball.rbgt.mapper.InstructionMapper;
import com.basketball.rbgt.mapper.RatioMapper;
import com.basketball.rbgt.pojo.Event;
import com.basketball.rbgt.pojo.Instruction;
import com.basketball.rbgt.pojo.Ratio;
import com.basketball.rbgt.pojo.dto.InstructionDTO;
import com.basketball.rbgt.service.InstructionService;
import com.basketball.rbgt.util.DateUtil;
import com.basketball.rbgt.util.DingUtil;
import com.github.houbb.opencc4j.util.CollectionUtil;
import com.github.houbb.opencc4j.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.hutool.cache.Cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private static final String cache_key = "SW_SUCCESS";
    private static final String cache_key_on = "ON";
    private static final String cache_key_off = "OFF";
    private static Cache<String, String> fifoCache = CacheUtil.newFIFOCache(10);


    /**
     * 新增下注指令
     *
     * @param event1
     * @param e
     * @param betSession
     */
    @Override
    public void add(Event event1, Event e, Integer betSession, String instructionId) {
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
            instruction.setInstructionId(instructionId);
            instruction.setBetSessionNumber(betSession);
            instruction.setBetAmount(Integer.parseInt(objects.get(betSession - 1) + ""));
            instruction.setBetHtn(event1.getName().split("VS")[0]);
            instruction.setBetAtn(event1.getName().split("VS")[1]);
            String[] split1 = event1.getQuizResults().split(",");
            if (1 == betSession || 5 == betSession) {
                instruction.setBetSessionName("总得分:滚球 单 / 双-第一节");
                if ("单".equals(split1[0])) {
                    instruction.setBetSingleOrDouble(1);
                } else {
                    instruction.setBetSingleOrDouble(2);
                }
            } else if (2 == betSession || 6 == betSession) {
                instruction.setBetSessionName("总得分:滚球 单 / 双-上半场");
                if ("单".equals(split1[0]) && "双".equals(split1[1])) {
                    // 上半场总和双
                    instruction.setBetSingleOrDouble(2);
                } else if ("单".equals(split1[0]) && "单".equals(split1[1])) {
                    // 上半场总和单
                    instruction.setBetSingleOrDouble(1);
                } else if ("双".equals(split1[0]) && "双".equals(split1[1])) {
                    // 上半场总和单
                    instruction.setBetSingleOrDouble(1);
                } else if ("双".equals(split1[0]) && "单".equals(split1[1])) {
                    // 上半场总和双
                    instruction.setBetSingleOrDouble(2);
                } else {
                    instruction.setBetSingleOrDouble(1);
                }
            } else if (3 == betSession || 7 == betSession) {
                instruction.setBetSessionName("总得分:滚球 单 / 双-第三节");
                if ("单".equals(split1[2])) {
                    instruction.setBetSingleOrDouble(1);
                } else {
                    instruction.setBetSingleOrDouble(2);
                }
            } else if (4 == betSession || 8 == betSession) {
                instruction.setBetSessionName("总得分:滚球 单 / 双");
                if ("单".equals(split1[0]) && "单".equals(split1[1]) && "单".equals(split1[2]) && "单".equals(split1[3])) {
                    // 总和单
                    instruction.setBetSingleOrDouble(1);
                } else if ("双".equals(split1[0]) && "单".equals(split1[1]) && "单".equals(split1[2]) && "单".equals(split1[3])) {
                    // 总和双
                    instruction.setBetSingleOrDouble(2);
                } else if ("单".equals(split1[0]) && "双".equals(split1[1]) && "单".equals(split1[2]) && "单".equals(split1[3])) {
                    // 总和双
                    instruction.setBetSingleOrDouble(2);
                } else if ("双".equals(split1[0]) && "双".equals(split1[1]) && "单".equals(split1[2]) && "单".equals(split1[3])) {
                    // 总和双
                    instruction.setBetSingleOrDouble(1);
                }
                if ("单".equals(split1[0]) && "单".equals(split1[1]) && "双".equals(split1[2]) && "单".equals(split1[3])) {
                    // 总和单
                    instruction.setBetSingleOrDouble(2);
                } else if ("双".equals(split1[0]) && "单".equals(split1[1]) && "双".equals(split1[2]) && "单".equals(split1[3])) {
                    // 总和双
                    instruction.setBetSingleOrDouble(1);
                } else if ("单".equals(split1[0]) && "双".equals(split1[1]) && "双".equals(split1[2]) && "单".equals(split1[3])) {
                    // 总和双
                    instruction.setBetSingleOrDouble(1);
                } else if ("双".equals(split1[0]) && "双".equals(split1[1]) && "双".equals(split1[2]) && "单".equals(split1[3])) {
                    // 总和双
                    instruction.setBetSingleOrDouble(2);
                } else if ("单".equals(split1[0]) && "单".equals(split1[1]) && "单".equals(split1[2]) && "双".equals(split1[3])) {
                    // 总和单
                    instruction.setBetSingleOrDouble(2);
                } else if ("双".equals(split1[0]) && "单".equals(split1[1]) && "单".equals(split1[2]) && "双".equals(split1[3])) {
                    // 总和双
                    instruction.setBetSingleOrDouble(1);
                } else if ("单".equals(split1[0]) && "双".equals(split1[1]) && "单".equals(split1[2]) && "双".equals(split1[3])) {
                    // 总和双
                    instruction.setBetSingleOrDouble(1);
                } else if ("双".equals(split1[0]) && "双".equals(split1[1]) && "单".equals(split1[2]) && "双".equals(split1[3])) {
                    // 总和双
                    instruction.setBetSingleOrDouble(2);
                }
                if ("单".equals(split1[0]) && "单".equals(split1[1]) && "双".equals(split1[2]) && "双".equals(split1[3])) {
                    // 总和单
                    instruction.setBetSingleOrDouble(1);
                } else if ("双".equals(split1[0]) && "单".equals(split1[1]) && "双".equals(split1[2]) && "双".equals(split1[3])) {
                    // 总和双
                    instruction.setBetSingleOrDouble(2);
                } else if ("单".equals(split1[0]) && "双".equals(split1[1]) && "双".equals(split1[2]) && "双".equals(split1[3])) {
                    // 总和双
                    instruction.setBetSingleOrDouble(2);
                } else if ("双".equals(split1[0]) && "双".equals(split1[1]) && "双".equals(split1[2]) && "双".equals(split1[3])) {
                    // 总和双
                    instruction.setBetSingleOrDouble(1);
                } else {
                    instruction.setBetSingleOrDouble(1);
                }
            }
            instruction.setCreateTime(new Date());
            instruction.setBetStatus(1);
            instruction.setBetNumber(0);
            instruction.setBetSession(betSession >= 5 ? betSession - 4 : betSession);
            instruction.setBetTime(DateUtil.getDate(0));
            instructionMapper.insert(instruction);
            if (betSession == 5 && StringUtils.isNotBlank(instructionId)) {
                Instruction instruction1 = instructionMapper.selectById(instructionId);
                instruction1.setBetStatus(4);
                instructionMapper.updateById(instruction1);
            }
            log.info("插入第" + betSession + "节下注指令 - > {},{}", e.getName(), e.getStartTime());
        }
    }

    /**
     * 校验支付指令是否红单
     *
     * @param event1
     * @param e
     * @return
     */
    @Override
    public Boolean checkInstruction(Event event1, Event e, Integer betSession) {
        int k1, k2 = 0;
        String[] splitf = null;
        if (betSession == 1 || betSession == 5) {
            splitf = e.getPeriodOne().split(":");
        } else if (betSession == 2 || betSession == 6) {
            splitf = e.getPeriodTow().split(":");
        } else if (betSession == 3 || betSession == 7) {
            splitf = e.getPeriodThree().split(":");
        } else if (betSession == 4 || betSession == 8) {
            splitf = e.getPeriodFour().split(":");
        }
        k1 = Integer.parseInt(splitf[0]);
        k2 = Integer.parseInt(splitf[1]);
        String[] splitb = event1.getQuizResults().split(",");
        String ds = "双";
        if ((k1 + k2) % 2 == 1) {
            ds = "单";
        }
        QueryWrapper<Instruction> qw = new QueryWrapper<Instruction>();
        qw.eq("bet_htn", event1.getName().split("VS")[0]).eq("bet_time", DateUtil.getDate(0)).eq("bet_status", 2).eq("bet_session", betSession);
        List<Instruction> is = instructionMapper.selectList(qw);
        if (is.size() == 1) {
            Instruction instruction = is.get(0);
            log.info("我是对比单双数据 -> {},{}",betSession,pd(splitb,betSession));
            if (ds.equals(pd(splitb,betSession))) {
                // 更新第一节的下注指令
                instruction.setBetStatus(3);
                instructionMapper.updateById(instruction);
                return true;
            } else {
                // 更新第一节的下注指令
                instruction.setBetStatus(4);
                instructionMapper.updateById(instruction);
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 判断单双
     * @param split1
     * @param betSession
     * @return
     */
    private String pd(String[] split1,int betSession){
        if (1 == betSession || 5 == betSession) {
            if ("单".equals(split1[0])) {
                return "单";
            } else {
                return "双";
            }
        } else if (2 == betSession || 6 == betSession) {
            if ("单".equals(split1[0]) && "双".equals(split1[1])) {
                // 上半场总和双
                return "双";
            } else if ("单".equals(split1[0]) && "单".equals(split1[1])) {
                // 上半场总和单
                return "单";
            } else if ("双".equals(split1[0]) && "双".equals(split1[1])) {
                // 上半场总和单
                return "单";
            } else if ("双".equals(split1[0]) && "单".equals(split1[1])) {
                // 上半场总和双
                return "双";
            } else {
                return "单";
            }
        } else if (3 == betSession || 7 == betSession) {
            if ("单".equals(split1[2])) {
                return "单";
            } else {
                return "双";
            }
        } else if (4 == betSession || 8 == betSession) {
            if ("单".equals(split1[0]) && "单".equals(split1[1]) && "单".equals(split1[2]) && "单".equals(split1[3])) {
                // 总和单
                return "单";
            } else if ("双".equals(split1[0]) && "单".equals(split1[1]) && "单".equals(split1[2]) && "单".equals(split1[3])) {
                // 总和双
                return "双";
            } else if ("单".equals(split1[0]) && "双".equals(split1[1]) && "单".equals(split1[2]) && "单".equals(split1[3])) {
                // 总和双
                return "双";
            } else if ("双".equals(split1[0]) && "双".equals(split1[1]) && "单".equals(split1[2]) && "单".equals(split1[3])) {
                // 总和双
                return "单";
            }
            if ("单".equals(split1[0]) && "单".equals(split1[1]) && "双".equals(split1[2]) && "单".equals(split1[3])) {
                // 总和单
                return "双";
            } else if ("双".equals(split1[0]) && "单".equals(split1[1]) && "双".equals(split1[2]) && "单".equals(split1[3])) {
                // 总和双
                return "单";
            } else if ("单".equals(split1[0]) && "双".equals(split1[1]) && "双".equals(split1[2]) && "单".equals(split1[3])) {
                // 总和双
                return "单";
            } else if ("双".equals(split1[0]) && "双".equals(split1[1]) && "双".equals(split1[2]) && "单".equals(split1[3])) {
                // 总和双
                return "双";
            } else if ("单".equals(split1[0]) && "单".equals(split1[1]) && "单".equals(split1[2]) && "双".equals(split1[3])) {
                // 总和单
                return "双";
            } else if ("双".equals(split1[0]) && "单".equals(split1[1]) && "单".equals(split1[2]) && "双".equals(split1[3])) {
                // 总和双
                return "单";
            } else if ("单".equals(split1[0]) && "双".equals(split1[1]) && "单".equals(split1[2]) && "双".equals(split1[3])) {
                // 总和双
                return "单";
            } else if ("双".equals(split1[0]) && "双".equals(split1[1]) && "单".equals(split1[2]) && "双".equals(split1[3])) {
                // 总和双
                return "双";
            }
            if ("单".equals(split1[0]) && "单".equals(split1[1]) && "双".equals(split1[2]) && "双".equals(split1[3])) {
                // 总和单
                return "单";
            } else if ("双".equals(split1[0]) && "单".equals(split1[1]) && "双".equals(split1[2]) && "双".equals(split1[3])) {
                // 总和双
                return "双";
            } else if ("单".equals(split1[0]) && "双".equals(split1[1]) && "双".equals(split1[2]) && "双".equals(split1[3])) {
                // 总和双
                return "双";
            } else if ("双".equals(split1[0]) && "双".equals(split1[1]) && "双".equals(split1[2]) && "双".equals(split1[3])) {
                // 总和双
                return "单";
            }
        }
        return "单";
    }

    /**
     * 校验支付指令是否已经存在红单
     *
     * @param event1
     * @param e
     * @return
     */
    @Override
    public Boolean checkInstructionRed(Event event1, Event e) {
        QueryWrapper<Instruction> qw = new QueryWrapper<Instruction>();
        qw.eq("bet_htn", event1.getName().split("VS")[0]).eq("bet_time", DateUtil.getDate(0)).eq("bet_status", 3);
        List<Instruction> is = instructionMapper.selectList(qw);
        if (is.size() >= 1) {
            return true;
        }
        return false;
    }

    /**
     * 根据状态和时间获取下注指令
     *
     * @return
     */
    @Override
    public List<Instruction> getByStatus() {
        if(null != fifoCache.get(cache_key) && cache_key_off.equals(fifoCache.get(cache_key))){
            log.info("航行者，已关闭下注权限，请注意！！");
            return new ArrayList<>();
        }
        QueryWrapper<Instruction> qw = new QueryWrapper<Instruction>();
        qw.eq("bet_time", DateUtil.getDate(0)).eq("bet_status", 1);
        List<Instruction> is = instructionMapper.selectList(qw);
        // 不为空时判断
        if (!CollectionUtil.isEmpty(is)) {
            String fc = fifoCache.get(is.get(0).getId());
            System.out.println("我是查询出来的缓存：" + fc);
            if (StrUtil.isNotBlank(fc) && Integer.parseInt(fc) == 6) {
                Instruction instruction = is.get(0);
                instruction.setBetStatus(4);
                instruction.setNote("下注次数，超过六次，停止下注");
                instructionMapper.updateById(instruction);
                fifoCache.remove(is.get(0).getId());
                DingUtil d = new DingUtil();
                d.sendMassage("["+instruction.getBetHtn()+" VS "+instruction.getBetAtn()+"] 下注次数，超过六次，停止下注");
            } else {
                fc = fc == null ? "0" : fc;
                fifoCache.put(is.get(0).getId(), (Integer.parseInt(fc)+1)+"");
                List<Instruction> list = new ArrayList<>();
                list.add(is.get(0));
                return list;
            }
        }
        return new ArrayList<>();
    }

    @Override
    public List<Instruction> getByStatusMore() {
        QueryWrapper<Instruction> qw = new QueryWrapper<Instruction>();
        qw.eq("bet_time", DateUtil.getDate(0)).eq("bet_status", 1);
        List<Instruction> is = instructionMapper.selectList(qw);
        // 不为空时判断
        if (!CollectionUtil.isEmpty(is)) {
            List<Instruction> list = new ArrayList<>();
            is.stream().forEach(i-> {
                String fc = fifoCache.get(i.getId());
                System.out.println("我是查询出来的缓存：" + fc);
                if (StrUtil.isNotBlank(fc) && Integer.parseInt(fc) == 6) {
                    Instruction instruction = i;
                    instruction.setBetStatus(4);
                    instruction.setNote("下注次数，超过六次，停止下注");
                    instructionMapper.updateById(instruction);
                    fifoCache.remove(is.get(0).getId());
                    DingUtil d = new DingUtil();
                    d.sendMassage("["+instruction.getBetHtn()+" VS "+instruction.getBetAtn()+"] 下注次数，超过六次，停止下注");
                } else {
                    fc = fc == null ? "0" : fc;
                    fifoCache.put(i.getId(), (Integer.parseInt(fc)+1)+"");
                    list.add(i);
                }
            });
            return list;
        }
        return new ArrayList<>();
    }

    /**
     * 更新下注失败赛事
     *
     * @param instructionId
     */
    @Override
    public void updateBetError(String instructionId) {
        // 查询支付指令
        Instruction instruction = instructionMapper.selectById(instructionId);
        if (ObjectUtil.isNotNull(instruction)) {
            // 设置支付指令失败次数
            instruction.setBetNumber(instruction.getBetNumber() + 1);
            // 更新支付指令失败次数+1
            instructionMapper.updateById(instruction);
        }
    }

    /**
     * 更新下注成功赛事
     *
     * @param instructionId
     */
    @Override
    public void updateBetSuccess(String instructionId) {
        // 查询支付指令
        Instruction instruction = instructionMapper.selectById(instructionId);
        if (ObjectUtil.isNotNull(instruction)) {
            // 设置支付指令失败次数
            instruction.setBetStatus(2);
            // 更新支付指令为已购买
            instructionMapper.updateById(instruction);
        }
    }

    @Override
    public void switchOn() {
        fifoCache.put(cache_key,cache_key_on);
        DingUtil d = new DingUtil();
        d.shopkeeperMassage("航行者，【已开启】下注权限，请注意！！");
    }

    @Override
    public void switchOff() {
        fifoCache.put(cache_key,cache_key_off);
        DingUtil d = new DingUtil();
        d.shopkeeperMassage("航行者，【已关闭】下注权限，请注意！！");
    }

    @Override
    public String getSwitchOff() {
        if(null == fifoCache.get(cache_key)){
            return cache_key_on;
        }
        return fifoCache.get(cache_key);
    }

    @Override
    public List<InstructionDTO> getToday() {
        QueryWrapper<Instruction> qw = new QueryWrapper<Instruction>();
        qw.eq("bet_time", DateUtil.getDate(0)).orderByAsc("create_time");
        List<Instruction> is = instructionMapper.selectList(qw);
        if(CollectionUtil.isEmpty(is)){
            return null;
        }
        List<InstructionDTO> list = new ArrayList<>();
        // 去重的数据
        List<String> collect = is.stream().map(Instruction::getBetAtn).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        // 将数据复制至DTO里
        for(String str : collect){
            List<Instruction> li = new ArrayList<>();
            InstructionDTO instructionDTO = new InstructionDTO();
            is.stream().forEach(i -> {
                if(str.equals(i.getBetAtn()))
                    li.add(i);
            });
            instructionDTO.setList(li);
            list.add(instructionDTO);
        }
        // 手动设置数据
        list.stream().forEach(l -> {
            l.setBetAtn(l.getList().get(0).getBetAtn());
            l.setBetHtn(l.getList().get(0).getBetHtn());
            l.setBetSession(l.getList().size());
            if(l.getList().size() == 1){
                if(l.getList().get(0).getBetStatus() == 1){
                    l.setBetStatusName("未下注");
                    l.getList().get(0).setInstructionId("未下注");
                }else if(l.getList().get(0).getBetStatus() == 2){
                    l.setBetStatusName("已下注");
                    l.getList().get(0).setInstructionId("已下注");
                }else if(l.getList().get(0).getBetStatus() == 3){
                    l.setBetStatusName("红");
                    l.getList().get(0).setInstructionId("红");
                }else{
                    l.setBetStatusName("黑");
                    l.getList().get(0).setInstructionId("黑");
                }
            }
            if(l.getList().size() == 2){
                l.getList().get(0).setInstructionId("黑");
                if(l.getList().get(1).getBetStatus() == 1){
                    l.setBetStatusName("未下注");
                    l.getList().get(1).setInstructionId("未下注");
                }else if(l.getList().get(1).getBetStatus() == 2){
                    l.setBetStatusName("已下注");
                    l.getList().get(1).setInstructionId("已下注");
                }else if(l.getList().get(1).getBetStatus() == 3){
                    l.setBetStatusName("红");
                    l.getList().get(1).setInstructionId("红");
                }else{
                    l.setBetStatusName("黑");
                    l.getList().get(1).setInstructionId("黑");
                }
            }
            if(l.getList().size() == 3){
                l.getList().get(0).setInstructionId("黑");
                l.getList().get(1).setInstructionId("黑");
                if(l.getList().get(2).getBetStatus() == 1){
                    l.setBetStatusName("未下注");
                    l.getList().get(2).setInstructionId("未下注");
                }else if(l.getList().get(2).getBetStatus() == 2){
                    l.setBetStatusName("已下注");
                    l.getList().get(2).setInstructionId("已下注");
                }else if(l.getList().get(2).getBetStatus() == 3){
                    l.setBetStatusName("红");
                    l.getList().get(2).setInstructionId("红");
                }else{
                    l.setBetStatusName("黑");
                    l.getList().get(2).setInstructionId("黑");
                }
            }
            if(l.getList().size() == 4){
                l.getList().get(0).setInstructionId("黑");
                l.getList().get(1).setInstructionId("黑");
                l.getList().get(2).setInstructionId("黑");
                if(l.getList().get(3).getBetStatus() == 1){
                    l.setBetStatusName("未下注");
                    l.getList().get(3).setInstructionId("未下注");
                }else if(l.getList().get(3).getBetStatus() == 2){
                    l.setBetStatusName("已下注");
                    l.getList().get(3).setInstructionId("已下注");
                }else if(l.getList().get(3).getBetStatus() == 3){
                    l.setBetStatusName("红");
                    l.getList().get(3).setInstructionId("红");
                }else{
                    l.setBetStatusName("黑");
                    l.getList().get(3).setInstructionId("黑");
                }
            }
        });
        return list;
    }
}
