package com.basketball.rbgt.service.impl;

import com.alibaba.fastjson.JSON;
import com.basketball.rbgt.mapper.RatioMapper;
import com.basketball.rbgt.pojo.Ratio;
import com.basketball.rbgt.service.RatioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 俞春旺
 */
@Slf4j
@Service
public class RatioServiceImpl implements RatioService {

    @Autowired
    private RatioMapper ratioMapper;

    /**
     * 描述：自动计算倍率
     * @param startBetPrice 开始投注金额
     * @param betRatio 倍率
     * @return
     */
    @Override
    public int calculateRatio(int startBetPrice, Double betRatio) {
        log.info("开始计算倍率，并自动保存库");
        Ratio r = new Ratio();
        r.setStartBetPrice(startBetPrice);
        r.setBetRatio(betRatio);
        r.setBetRange(getBetRange(startBetPrice,betRatio));
        int insert = ratioMapper.insert(r);
        return insert;
    }

    /**
     * 描述：计算倍率工具
     * @param startBetPrice
     * @param betRatio
     * @return
     */
    private static String getBetRange(int startBetPrice, Double betRatio){
        log.info("计算倍率区间 ==== 开始");
        // 循环计算倍率次数
        int cycle = 12;
        Double price = 0.00;
        int k = 1;
        List<Double> br = new ArrayList<>();
        for(int i = 0;i < cycle;i++){
            if(i == 0){
                System.out.println("第一次计算");
                price = Double.parseDouble(startBetPrice+"");
            }else{
                // 30,60,120,
                price = (double) Math.round((startBetPrice * Math.pow(2, i) ) / betRatio * 1) / 1;;
            }
            br.add(price);
        }
        System.out.println(JSON.toJSONString(br));
        log.info("计算倍率区间 ==== 结束");
        return JSON.toJSONString(br);
    }
}
