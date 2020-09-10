package com.basketball.rbgt.service;

import com.basketball.rbgt.pojo.Event;
import com.basketball.rbgt.pojo.Instruction;
import com.basketball.rbgt.pojo.Ratio;
import com.basketball.rbgt.pojo.dto.InstructionDTO;

import java.util.List;

/**
 * @author 俞春旺
 */
public interface InstructionService {

    /**
     * 新增下注指令
     * @param event1
     * @param e
     * @param betSession
     */
    void add(Event event1, Event e,Integer betSession,String instructionId);

    /**
     * 校验支付指令是否红单
     * @param event1
     * @param e
     * @param betSession
     * @return
     */
    Boolean checkInstruction(Event event1, Event e,Integer betSession);

    /**
     * 校验支付指令是否已经存在红单
     * @param event1
     * @param e
     * @return
     */
    Boolean checkInstructionRed(Event event1, Event e);

    /**
     * 根据状态和时间获取下注指令
     * @return
     */
    List<Instruction> getByStatus();

    /**
     * 根据状态和时间获取下注指令
     * @return
     */
    List<Instruction> getByStatusMore();

    /**
     * 根据状态和时间获取下注指令
     * @return
     */
    List<InstructionDTO> getToday();

    /**
     * 更新下注失败赛事
     * @param instructionId
     */
    void updateBetError(String instructionId);

    /**
     * 更新下注成功赛事
     * @param instructionId
     */
    void updateBetSuccess(String instructionId);
}
