package com.ruoyi.flow.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class FlowModel {
    private String strBPM;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;
    /**
     * 业务对象ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long formObjectId;

    /**
     * 模型ID
     */
    private String deploymentId;

    /**
     * 发布后的ID
     */
    private String processDefinitionId;

    private String status;

    private String config;

}
