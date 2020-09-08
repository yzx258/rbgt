package com.basketball.rbgt.pojo.dto;

import com.basketball.rbgt.pojo.Instruction;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @company：厦门宜车时代信息技术有限公司
 * @copyright：Copyright (C), 2020
 * @author：yucw
 * @date：2020/9/7 14:31
 * @version：1.0
 * @description: 下注指令
 */
@Data
public class InstructionDTO {

    /**
     * 下注主队：湖人
     */
    @ApiModelProperty(value = "主队名称")
    private String betHtn;

    /**
     * 下注客队：快船
     */
    @ApiModelProperty(value = "客队名称")
    private String betAtn;

    /**
     * 下注状态：1：需要购买；2：已购买；3：已红单[携带备注，说明下注次数过多]；4：已黑单；5：四节全黑
     */
    @ApiModelProperty(value = "总的下注状态")
    private Integer betStatus;

    /**
     * 下注场次：1：第一节；2：第二节；3：第三节；4：第四节
     */
    @ApiModelProperty(value = "下注场次")
    private Integer betSession;

    @ApiModelProperty(value = "下注指令")
    private List<Instruction> list;

}
