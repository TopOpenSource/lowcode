package com.ruoyi.system.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ObjectDTO {
    /**
     *
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 业务对象名称
     */
    private String name;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 是否包含流程
     */
    private String isFlow;

    /**
     * 流程定义ID
     */
    private String processDefinitionId;

    private String deployId;

    private String type;

    private String icon;
}
