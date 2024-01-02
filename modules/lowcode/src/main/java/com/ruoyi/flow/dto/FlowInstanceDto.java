package com.ruoyi.flow.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 业务办理 列表对象
 */

@Data
public class FlowInstanceDto {
    //业务对象id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectId;
    //业务对象 名称
    private String objectName;

    //业务对象实例ID
    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectInstanceId;
    //业务对象实例内容
    private String objectInstanceContent;

    //流程实例ID
    private String procInstId;

    //任务ID
    private String taskId;
    //任务名称
    private String taskName;

    private String applyId;
    //标题
    private String comment;
    //流程实例配置
    private String flowIncConfig;

    public FlowInstanceDto() {

    }

    public FlowInstanceDto(Long objectId, Long objectInstanceId, String procInstId, String taskId, String taskName, String applyId, String comment) {
        this.objectId = objectId;
        this.objectInstanceId = objectInstanceId;
        this.procInstId = procInstId;
        this.taskId = taskId;
        this.taskName = taskName;
        this.applyId = applyId;
        this.comment = comment;
    }
}
