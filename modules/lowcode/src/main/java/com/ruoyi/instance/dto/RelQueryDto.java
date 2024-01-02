package com.ruoyi.instance.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 查询关联实体
 */
@Data
public class RelQueryDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long  targetObjId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long  sourceAttrId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long  sourceInstId;

    private String tableColumn;
}
