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
 * 医疗记录存储实体类
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@Getter
@Setter
@TableName("medicalrecords")
@ApiModel(value = "MedicalRecord对象", description = "医疗记录信息")
@ToString
public class MedicalRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("医疗记录ID")
    @TableId(value = "medicalRecordId", type = IdType.AUTO)
    @TableField("medicalRecordId")
    private Integer medicalRecordId;

    @ApiModelProperty("患者ID")
    @TableField("patientId")
    private Integer patientId;

    @ApiModelProperty("记录日期")
    @TableField("record_date")
    private Date recordDate;

    @ApiModelProperty("记录文本")
    @TableField("record_text")
    private String recordText;

    public void setId(Integer medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public Serializable getId() {
        return this.medicalRecordId;
    }
}