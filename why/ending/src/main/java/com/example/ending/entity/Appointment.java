package com.example.ending.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 预约信息存储实体类
 * </p>
 *
 * @since 2023-05-06
 */
@Getter
@Setter
@TableName("appointments")
@ApiModel(value = "Appointment对象", description = "预约信息")
@ToString
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("预约ID")
    @TableId(value = "appointmentId", type = IdType.AUTO)
    private Integer appointmentId;

    @ApiModelProperty("患者ID")
    @TableField("patientId")
    private Integer patientId;

    @ApiModelProperty("医生ID")
    @TableField("doctorId")
    private Integer doctorId;

    @ApiModelProperty("预约日期")
    @TableField("appointment_date")
    private Date appointmentDate;

    @ApiModelProperty("开始时间")
    @TableField("start_time")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @TableField("end_time")
    private Date endTime;
}
