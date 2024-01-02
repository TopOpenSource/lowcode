package com.ruoyi.form.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class LayoutTableDto extends BaseModel {
    /**
     *
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     *
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectId;
    /**
     * 列表布局名称
     */
    private String name;
    /**
     * 操作
     */
    private String operation;

    /**
     * 是否启用
     */
    private String enable;

    /**
     * 是否默认
     */
    private String isDefault;

    /**************************************/
    /**
     * 修改人姓名
     */
    private String modifiedName;

    /**
     * 视图
     */
    private List<String> viewIds;


    @JsonSerialize(using = ToStringSerializer.class)
    private String viewId;
    /**
     * table_view关联表id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tableViewId;

    private String index;
}

