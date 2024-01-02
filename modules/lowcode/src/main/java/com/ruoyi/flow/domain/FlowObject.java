package com.ruoyi.flow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@TableName("lowcode_workflow")
@EqualsAndHashCode(callSuper = false)
@Data
public class FlowObject extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;
    /**
     * 业务对象ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "form_object_id")
    private Long formObjectId;

    /**
     * 模型ID
     */
    @TableField(value = "deploy_id")
    private String deploymentId;

    /**
     * 发布后的ID
     */
    @TableField(value = "proc_def_id")
    private String processDefinitionId;

    @TableField(value = "flow_config")
    private String config;


    public void setValue(Long formObjectId,String deploymentId,String processDefinitionId){
        this.formObjectId=formObjectId;
        this.deploymentId=deploymentId;
        this.processDefinitionId=processDefinitionId;
    }
}
