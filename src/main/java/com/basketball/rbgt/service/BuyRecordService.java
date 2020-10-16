package com.basketball.rbgt.service;

import com.basketball.rbgt.pojo.BuyRecord;
import com.basketball.rbgt.pojo.spec.BuyRecordSpec;

import java.util.List;

/**
 * @author 俞春旺
 */
public interface BuyRecordService {


    /**
     * 描述 ： 新增下注记录
     * @param spec
     */
    void add(BuyRecordSpec spec);

    /**
     * 描述：查询黑单
     * @param type
     * @return
     */
    List<BuyRecord> get(String type);

}
