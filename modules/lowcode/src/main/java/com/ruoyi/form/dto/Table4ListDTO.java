package com.ruoyi.form.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @author cheng
 * 列表页用
 */
@Data
public class Table4ListDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String operationsStr;

    private List<Operation4ListDTO> operations;

    private List<View4ListDTO> views;
}
