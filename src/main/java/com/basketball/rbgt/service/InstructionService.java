package com.basketball.rbgt.service;

import com.basketball.rbgt.pojo.Event;
import com.basketball.rbgt.pojo.Ratio;

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
    void add(Event event1, Event e,Integer betSession);

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
}
