package com.ruoyi.form.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ruoyi.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yangzz
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ObjectAttributeDTO extends BaseModel {
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
     * 数据库存储类型
     */
    private String dbType;

    /**
     * 数据库存储最大长度
     */
    private Integer dbLength;

    /**
     * 小数点几位
     */
    private Integer decimals;
    /**
     * 界面展示类型
     */
    private String displayType;

    /**
     * 是否平铺
     */
    private String displayFlat;
    /**
     * 界面展示提示
     */
    private String displayTitle;
    /**
     * 界面展示正则表达式
     */
    private String displayRegex;
    /**
     * 界面显示最小长度
     */
    private Integer displayMinlength;
    /**
     * 是否不为空 0可以为空 1不为空
     */
    private String notnull;
    /**
     * 是否唯一    0 不唯一 1 唯一
     */
    private String unique;

    /**
     * 状态 0 禁用 1正常
     */
    private String enable;

    private String isDefault;

    private String displayFullline;

    /*补充*/
    private String typeName;

    private String alias;
    private String multiple;

    private String tableName;
}
