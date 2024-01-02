package com.ruoyi.flow.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.core.web.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * flowalbe 任务
 */
@Data
public class FlowTaskDto extends BaseEntity {
    //业务对象id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectId;
    //业务对象
    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectName;
    //业务对象实例ID
    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectInstanceId;
    //流程实例ID
    private String procInstId;

    //任务ID
    private String taskId;
    //任务名称
    private String taskName;
    //代办人
    private String taskAssignee;
    //标题
    private String comment;

    private String deployId;

    private String procDefId;

    //申请人ID
    private String applyId;

    private String applyName;

    private Date applyTime;

    private Date createTime;

    private String executionId;

    private String actId;

    private String taskDefKey;
    /**
     * 退回至任务key
     */
    private String targetKey;

    private String taskAssignees;



}
