package com.basketball.rbgt.pojo.spec;

import lombok.Data;

/**
 * @author 俞春旺
 */
@Data
public class RatioSpec {

    /**
     * 开始投注金额
     */
    private int startBetPrice;
    /**
     * 倍率
     */
    private Double betRatio;
}
