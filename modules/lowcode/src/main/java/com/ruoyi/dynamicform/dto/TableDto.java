package com.ruoyi.dynamicform.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.form.domain.FormObjectAttribute;
import com.ruoyi.form.domain.LayoutView;
import lombok.Data;

import java.util.List;

/**
 *  表格
 */
@Data
public class TableDto {
    private List<FormObjectAttribute> attrs;

    private List<LayoutView> views;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long viewId;
}
