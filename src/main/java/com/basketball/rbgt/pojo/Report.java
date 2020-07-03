package com.basketball.rbgt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 俞春旺
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Report implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 唯一ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 年份
     */
    private String year;

    /**
     * 月份
     */
    private String month;

    /**
     * 竞猜类型：0：黑；1：红
     */
    private Integer quizType;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 笔数
     */
    private Integer amount;


}
