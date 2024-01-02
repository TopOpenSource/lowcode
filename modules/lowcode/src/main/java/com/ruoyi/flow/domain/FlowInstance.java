package com.ruoyi.flow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 业务办理 列表对象
 */

@TableName("lowcode_workflow_instance")
@Data
public class FlowInstance extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    //业务对象id
    @TableField(value = "object_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectId;

    //业务对象实例ID
    @TableField(value = "object_instance_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectInstanceId;

    //流程实例ID
    @TableField(value = "proc_inst_id")
    private String procInstId;
    //申请人
    @TableField(value = "apply_id")
    private String applyId;
    //申请时间
    @TableField(value = "apply_time")
    private Date applyTime;
    //标题
    @TableField(value = "comment")
    private String comment;

    @TableField(value = "deploy_id")
    private String deployId;

    @TableField(value = "proc_def_id")
    private String procDefId;

    //流程实例配置
    @TableField(value = "flow_inc_config")
    private String flowIncConfig;

    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_by")
    private String updateBy;

    @TableField(value = "create_by")
    private String createBy;

    @TableField(value = "is_complate")
    private String isComplate;

    /**
     * 当前环节名称
     */
    @TableField(value = "task_name")
    private String taskName;

    /**
     * 当前环节key
     */
    @TableField(value = "task_def_key")
    private String taskDefKey;

    /**
     * 当前环节待办人
     */
    @TableField(value = "task_assignees")
    private String taskAssignees;

    public FlowInstance() {

    }

    public FlowInstance(Long id,String procInstId,String deployId,String procDefId){
        this.id=id;
        this.procInstId = procInstId;
        this.deployId=deployId;
        this.procDefId=procDefId;
    }


    public FlowInstance(Long id, Long objectId, Long objectInstanceId,String applyId,String comment) {
        this.id = id;
        this.objectId = objectId;
        this.objectInstanceId = objectInstanceId;
        this.applyId=applyId;
        this.comment=comment;
    }
}
