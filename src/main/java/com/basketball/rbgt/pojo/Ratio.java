package com.basketball.rbgt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 俞春旺
 * @since 2020-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Ratio implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

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
    private Integer version;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 倍率类型：1：NBA；2：CBA；3：其他
     */
    private Integer type;

    /**
     * 倍率状态：1：使用中；2：废弃
     */
    private Integer status;

    /**
     * 开始投注金额：30
     */
    private Integer startBetPrice;

    /**
     * 计算投注倍率：0.94
     */
    private Double betRatio;

    /**
     * 投注区间：[30,60,120]
     */
    private String betRange;


}
