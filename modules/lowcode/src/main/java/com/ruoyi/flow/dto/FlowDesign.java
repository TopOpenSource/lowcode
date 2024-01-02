package com.ruoyi.flow.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class FlowDesign {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long  flowId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long formObjectId;
    private String deploymentId;
    private String processDefinitionId;
    private String name;
    private String xml;
    private String tenantId;
}
