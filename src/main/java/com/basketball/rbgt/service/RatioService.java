package com.basketball.rbgt.service;

import com.basketball.rbgt.pojo.Ratio;

/**
 * @author 俞春旺
 */
public interface RatioService {

    /**
     * 描述：自动计算倍率
     * @param startBetPrice 开始投注金额
     * @param betRatio 倍率
     * @return
     */
    int calculateRatio(int startBetPrice, Double betRatio);

}
