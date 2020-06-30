package com.basketball.rbgt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.basketball.rbgt.mapper.EventMapper;
import com.basketball.rbgt.pojo.Event;
import com.basketball.rbgt.service.EventService;
import org.springframework.stereotype.Service;

/**
 * @author 俞春旺
 */
@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements EventService{
}
