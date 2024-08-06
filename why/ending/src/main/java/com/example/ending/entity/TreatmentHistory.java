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
 * 治疗历史信息存储实体类
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@Getter
@Setter
@TableName("treatmenthistory")
@ApiModel(value = "TreatmentHistory对象", description = "治疗历史信息")
@ToString
public class TreatmentHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("治疗历史ID")
    @TableId(value = "treatmentHistoryId", type = IdType.AUTO)
    @TableField("treatmentHistoryId")
    private Integer treatmentHistoryId;

    @ApiModelProperty("治疗ID")
    @TableField("treatmentId")
    private Integer treatmentId;

    @ApiModelProperty("医生ID")
    @TableField("doctorId")
    private Integer doctorId;

    @ApiModelProperty("药品ID")
    @TableField("drugId")
    private Integer drugId;

    @ApiModelProperty("程序ID")
    @TableField("procedureId")
    private Integer procedureId;

    @ApiModelProperty("记录日期")
    @TableField("record_date")
    private Date recordDate;

    @ApiModelProperty("记录时间")
    @TableField("record_time")
    private Date recordTime;

    @ApiModelProperty("结果")
    private String result;

    public void setId(Integer treatmentHistoryId) {
        this.treatmentHistoryId = treatmentHistoryId;
    }

    public Serializable getId() {
        return this.treatmentHistoryId;
    }
}
