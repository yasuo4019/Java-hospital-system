package com.example.ending.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@TableName("procedures")
@ApiModel(value = "Procedure对象", description = "")
@ToString
public class Procedure implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Procedure ID")
    @TableId(value = "procedureId", type = IdType.AUTO)
    private Integer procedureId;

    @ApiModelProperty("Procedure name")
    private String name;

    @ApiModelProperty("Procedure type")
    private String type;

    @ApiModelProperty("Procedure description")
    private String description;
}
