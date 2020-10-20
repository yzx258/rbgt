package com.basketball.rbgt.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.basketball.rbgt.mapper.BuyRecordMapper;
import com.basketball.rbgt.pojo.BuyRecord;
import com.basketball.rbgt.pojo.spec.BuyRecordSpec;
import com.basketball.rbgt.service.BuyRecordService;
import com.basketball.rbgt.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @company： 厦门宜车时代信息技术有限公司
 * @copyright： Copyright (C), 2020
 * @author： yucw
 * @date： 2020/10/16 14:14
 * @version： 1.0
 * @description:
 */
@Service
@Slf4j
public class BuyRecordServiceImpl implements BuyRecordService {

    @Autowired
    private BuyRecordMapper mapper;

    @Override
    public void add(BuyRecordSpec spec) {
        BuyRecord br = new BuyRecord();
        br.setJson(spec.getJson());
        br.setType(spec.getType());
        br.setTime(DateUtil.getDate(0));
        br.setCreateTime(new Date());
        mapper.insert(br);
    }

    @Override
    public List<BuyRecord> get(String type) {
        QueryWrapper<BuyRecord> qw = new QueryWrapper<>();
        qw.eq("type",type);
        return mapper.selectList(qw);
    }

    @Override
    public void del(String type) {
        List<BuyRecord> buyRecords = this.get(type);
        if(buyRecords.size() > 0){
            BuyRecord buyRecord = buyRecords.get(0);
            buyRecord.setType(0);
            mapper.updateById(buyRecord);
        }
    }
}
