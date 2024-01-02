package com.ruoyi.form.dto;

import com.ruoyi.common.BaseModel;
import lombok.Data;

@Data
public class FormObjectAttributeDto extends BaseModel {

    /**
     * 字段名称
     */
    private String name;
    /**
     * 字段类型
     */
    private String type;
    /**
     * 关联字典类型
     */
    private String dictTypeCode;
    /**
     * 界面展示类型
     */
    private String displayType;

    private String alias;
}
