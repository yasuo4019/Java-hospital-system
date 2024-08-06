package com.example.ending.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 病人信息存储实体类
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@Getter
@Setter
@TableName("patients")
@ApiModel(value = "Patient对象", description = "病人信息")
@ToString
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("病人ID")
    @TableId(value = "patientId", type = IdType.AUTO)
    private Integer patientId;

    @ApiModelProperty("病人姓名")
    private String name;

    @ApiModelProperty("病人性别")
    private String gender;

    @ApiModelProperty("病人出生日期")
    private Date birthday;

    @ApiModelProperty("住院日期")
    private Date HospitalizationDate;

    @ApiModelProperty("出院日期")
    private Date DischargeDate;


    public Integer getId() {
        return patientId;
    }
}
