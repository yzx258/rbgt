package com.basketball.rbgt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 俞春旺
 * @since 2020-06-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Event implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 比赛时间
     */
    private String eventTime;
    /**
     * 比赛日期
     */
    private String startTime;

    /**
     * 比赛名称
     */
    private String name;

    /**
     * 竞猜结果
     */
    private String quizResults;

    /**
     * 赛事结果
     */
    private String results;

    /**
     * 赛事第一节
     */
    @TableField("periodOne")
    private String periodOne;

    /**
     * 赛事第二节
     */
    @TableField("periodTow")
    private String periodTow;

    /**
     * 赛事第三节
     */
    @TableField("periodThree")
    private String periodThree;

    /**
     * 赛事第四节
     */
    @TableField("periodFour")
    private String periodFour;

    /**
     * 赛事第加时一节
     */
    @TableField("overTimeOne")
    private String overTimeOne;

    /**
     * 赛事第加时二节
     */
    @TableField("overTimeTow")
    private String overTimeTow;

    /**
     * 赛事第加时三节
     */
    @TableField("overTimeThree")
    private String overTimeThree;

    /**
     * 赛事第加时四节
     */
    @TableField("overTimeFour")
    private String overTimeFour;

    /**
     * 赛事第加时五节
     */
    @TableField("overTimeFive")
    private String overTimeFive;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 版本号
     */
    @Version
    private String version;

    /**
     * 逻辑删除：【0：没删除；1：删除】
     */
    @TableLogic
    private Integer deleted;

    /**
     * 赛事类型
     */
    private Integer type;

    /**
     * 赛事类型名称
     */
    private String typeName;


}
