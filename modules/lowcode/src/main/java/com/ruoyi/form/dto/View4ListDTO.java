package com.ruoyi.form.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @author cheng
 * 列表页用-视图
 */
@Data
public class View4ListDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 视图名称
     */
    private String name;
}
