package com.ruoyi.form.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author cheng
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class LayoutTable extends BaseModel {
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
}
