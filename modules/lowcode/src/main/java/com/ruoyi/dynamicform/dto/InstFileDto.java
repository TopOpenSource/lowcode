package com.ruoyi.dynamicform.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class InstFileDto {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long instId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long attrId;

    private List<Long> idList;
}
