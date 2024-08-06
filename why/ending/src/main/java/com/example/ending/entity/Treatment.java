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
 * 治疗信息存储实体类
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@Getter
@Setter
@TableName("treatment")
@ApiModel(value = "Treatment对象", description = "治疗信息")
@ToString
public class Treatment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("治疗ID")
    @TableId(value = "treatmentId", type = IdType.AUTO)
    @TableField("treatmentId")
    private Integer treatmentId;

    @ApiModelProperty("患者ID")
    @TableField("patientId")
    private Integer patientId;

    @ApiModelProperty("医生ID")
    @TableField("doctorId")
    private Integer doctorId;

    @ApiModelProperty("部门ID")
    @TableField("departmentId")
    private Integer departmentId;

    @ApiModelProperty("治疗开始日期")
    @TableField("start_date")
    private Date startDate;

    @ApiModelProperty("治疗结束日期")
    @TableField("end_date")
    private Date endDate;

    @ApiModelProperty("诊断")
    private String diagnosis;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private Date createTime;


    public void setId(Integer treatmentId) {
        this.treatmentId = treatmentId;
    }

    public Serializable getId() {
        return this.treatmentId;
    }
}
