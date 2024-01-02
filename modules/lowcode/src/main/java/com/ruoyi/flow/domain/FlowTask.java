package com.ruoyi.flow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.BaseEntity;
import lombok.Data;

@TableName("lowcode_workflow_task")
@Data
public class FlowTask extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    //业务对象实例ID
    @TableField(value = "inst_id")
    private Long instId;

    //任务ID
    @TableField(value = "flow_task_id")
    private String taskId;
    //任务名称
    @TableField(value = "flow_task_name")
    private String taskName;
    //处理人
    @TableField(value = "task_assignee")
    private String taskAssignee;
    //评价
    @TableField(value = "comment")
    private String comment;

    @TableField(value = "check_state")
    private String checkState;


    public FlowTask() {

    }

    public FlowTask(Long id, Long instId, String taskId, String taskName, String taskAssignee, String comment) {
        this.id = id;
        this.instId = instId;
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskAssignee = taskAssignee;
        this.comment = comment;
    }

}
