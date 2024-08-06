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
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@Getter
@Setter
@TableName("doctors")
@ApiModel(value = "Doctor对象", description = "")
@ToString
public class Doctor implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("doctorId")
    @TableId(value = "doctorId", type = IdType.AUTO)
    private Integer doctorId;

    @ApiModelProperty("医生名字")
    private String name;

    @ApiModelProperty("医生类别")
    private String specialization;

    public Integer getId() {
        return doctorId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }
}
