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
public class Instruction implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

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
     * 下注金额
     */
    private Integer betAmount;

    /**
     * 下注单双
     */
    private Integer betSingleOrDouble;

    /**
     * 下注场次：1：第一节；2：第二节；3：第三节；4：第四节
     */
    private Integer betSession;

    /**
     * 下注场次名称：第一节 单双；第二节 单双；第三节 单双；第四节 单双
     */
    private String betSessionName;

    /**
     * 下注状态：1：需要购买；2：已购买；3：已红单[携带备注，说明下注次数过多]；4：已黑单；5：四节全黑
     */
    private Integer betStatus;

    /**
     * 下注主队：湖人
     */
    private String betHtn;

    /**
     * 下注客队：快船
     */
    private String betAtn;

    /**
     * 下注次数：失败次数，默认0
     */
    private Integer betNumber;

    /**
     * 下注时间：2020-08-23
     */
    private String betTime;

    /**
     * 携带五黑的支付指令
     */
    private String instructionId;

    /**
     * 备注
     */
    private String note;

}
