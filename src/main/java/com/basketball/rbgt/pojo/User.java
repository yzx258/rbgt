package com.basketball.rbgt.pojo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Setter
@Getter
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private int age;
    private String email;
    private String password;

    /**
     * 字段上添加填充内容
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 添加锁
      */
    @Version
    private Integer version;

}