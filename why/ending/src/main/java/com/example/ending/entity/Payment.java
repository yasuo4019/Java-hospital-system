package com.example.ending.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 支付信息存储实体类
 * </p>
 *
 * @since 2023-05-06
 */
@Getter
@Setter
@TableName("payments")
@ApiModel(value = "Payment对象", description = "支付信息")
@ToString
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("支付ID")
    @TableId(value = "paymentId", type = IdType.AUTO)
    private Integer paymentId;

    @ApiModelProperty("治疗ID")
    @TableField("treatmentId")
    private Integer treatmentId;

    @ApiModelProperty("支付金额")
    private BigDecimal amount;

    @ApiModelProperty("支付日期")
    @TableField("payment_date")
    private Date paymentDate;

    @ApiModelProperty("支付方式")
    @TableField("payment_method")
    private String paymentMethod;

}
