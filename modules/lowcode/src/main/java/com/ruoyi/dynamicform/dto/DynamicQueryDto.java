package com.ruoyi.dynamicform.dto;

import lombok.Data;

/**
 * 动态查询条件  mapper用
 */
@Data
public class DynamicQueryDto {
    /**
     * 表名
     */
    private String tableName;

    /**
     * 查询列
     */
    private String queryCols;

    /**
     * 查询条件
     */
    private String queryWhere;

    /**
     * 查询条件-高级查询
     */
    private String queryWhereAppend;

    /**
     * 正序 倒序
     */
    private String orderType;

    /**
     * 排序字段
     */
    private String orderBy;
}
