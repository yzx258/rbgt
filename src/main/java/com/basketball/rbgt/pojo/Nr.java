package com.basketball.rbgt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @company： 厦门宜车时代信息技术有限公司
 * @copyright： Copyright (C), 2020
 * @author： yucw
 * @date： 2020/9/10 11:22
 * @version： 1.0
 * @description: 替换表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Nr implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 唯一标识
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private String id;

    /**
     * 源名称
     */
    private String name;

    /**
     * 替换名称
     */
    private String target;

    /**
     * 类型名称
     */
    private String type;

    /**
     * 类型名称
     */
    private String time;

}
