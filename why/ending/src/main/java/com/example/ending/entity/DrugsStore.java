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

/**
 * <p>
 * 药品存储实体类
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@Getter
@Setter
@TableName("drugsstore")
@ApiModel(value = "DrugsStore对象", description = "药品存储信息")
@ToString
public class DrugsStore implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("药品ID")
    @TableId(value = "drugId", type = IdType.AUTO)
    private Integer drugId;

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("药品类型")
    private String type;

    @ApiModelProperty("药品数量")
    private String number;
}
