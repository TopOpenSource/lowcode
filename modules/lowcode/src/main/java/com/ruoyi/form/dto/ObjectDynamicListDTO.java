package com.ruoyi.form.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.form.domain.FormObjectAttribute;
import com.ruoyi.form.domain.LayoutView;
import com.ruoyi.form.domain.FormObjectOperation;
import lombok.Data;

import java.util.List;

/**
 * 业务对象动态列表主体类
 */
@Data
public class ObjectDynamicListDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;


    @JsonSerialize(using = ToStringSerializer.class)
    private Long layoutId;

    private String operation;  // 操作， xxx,xxx,xxx

    private List<LayoutView> views;     // 该布局下的视图

    private List<FormObjectOperation> operations;  // 操作list

    private List<FormObjectAttribute> attrs;  // 业务对象字段

}
