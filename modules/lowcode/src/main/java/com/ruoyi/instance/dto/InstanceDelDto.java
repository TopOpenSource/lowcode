package com.ruoyi.instance.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class InstanceDelDto {
    @JsonSerialize(using = ToStringSerializer.class)
    private List<Long> ids;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectId;
}
