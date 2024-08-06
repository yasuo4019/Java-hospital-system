package com.example.ending.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("doctorlocation")
@ApiModel(value = "DoctorLocation对象", description = "医生位置信息")
@ToString
public class DoctorLocation implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("医生ID")
    //@TableId(value = "doctorId", type = IdType.AUTO)
    @TableField("doctorId")
    private Integer doctorId;

    @ApiModelProperty("部门ID")
    @TableField("departmentId")
    private Integer departmentId;

    @ApiModelProperty("房间")
    @TableField("room")
    private String room;

    @ApiModelProperty("时间表")
    @TableField("timeTable")
    private String timeTable;

    @ApiModelProperty("医生等级")
    @TableField("doctorLevel")
    private String doctorLevel;

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

}
