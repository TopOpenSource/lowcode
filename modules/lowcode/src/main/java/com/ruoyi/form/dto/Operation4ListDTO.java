package com.ruoyi.form.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @author cheng
 * 列表页用-操作
 */
@Data
public class Operation4ListDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 操作名称
     */
    private String name;
    /**
     * 操作方法名
     */
    private String methodName;
}
