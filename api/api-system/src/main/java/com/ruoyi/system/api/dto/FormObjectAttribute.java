package com.ruoyi.system.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author system
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FormObjectAttribute extends BaseModel {
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

    private Integer displayMinlength;

    private String alias;

    /**
     * 是否多选 0否 1是
     */
    private String multiple;



}
