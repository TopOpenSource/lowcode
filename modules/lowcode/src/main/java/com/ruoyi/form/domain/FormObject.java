package com.ruoyi.form.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author system
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FormObject extends BaseModel {
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
     * 上线状态 0上线 1下线
     */
    private String state;

    private String type;

    private String icon;

    /**********非model字段***********/
    /**
     *
     */
    private List<FormObjectAttribute> attrs;

}
