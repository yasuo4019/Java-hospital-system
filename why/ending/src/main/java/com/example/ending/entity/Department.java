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
 * 部门信息存储实体类
 * </p>
 *
 * @author 王宏远
 * @since 2023-05-06
 */
@Getter
@Setter
@TableName("departments")
@ApiModel(value = "Department对象", description = "部门信息")
@ToString
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("部门ID")
    @TableId(value = "departmentId", type = IdType.AUTO)
    private Integer departmentId;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("部门描述")
    private String description;

    public Integer getId() {
        return departmentId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }
}
