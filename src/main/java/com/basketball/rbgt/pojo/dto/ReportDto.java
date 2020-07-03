package com.basketball.rbgt.pojo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author yucw
 * @date 2020/7/3 16:33
 */
@Data
@Setter
@Getter
public class ReportDto {

    /**
     * 年份
     */
    private String year;

    /**
     * 月份
     */
    private String month;

    /**
     * 总笔数
     */
    private Integer allAmount;

    /**
     * 红单数
     */
    private Integer redAmount;

    /**
     * 红单比
     */
    private String redRatio;

    /**
     * 黑单数
     */
    private Integer blackAmount;

    /**
     * 黑单比
     */
    private String blackRatio;

}
