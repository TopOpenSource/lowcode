package com.ruoyi.form.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author system
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class FormObjectOperation extends BaseModel {
    /**
     *
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 业务对象ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long objectId;
    /**
     * 操作名称
     */
    private String name;
    /**
     * 操作方法名
     */
    private String methodName;
}
