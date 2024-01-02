package com.ruoyi.form.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yangzz
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class AttributeType {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 字段类型名称
     */
    private String name;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 数据库存储类型
     */
    private String dbType;

    /**
     * 数据库存储最大长度
     */
    private String dbLength;

    /**
     * 数据存储小数点位数
     */
    private String decimals;

    /**
     * 界面展示类型
     */
    private String displayType;

    /**
     * 界面展示正则表达式
     */
    private String displayRegex;

    /**
     * 是否整行显示 0 否， 1 是
     */
    private String displayFullline;
}
