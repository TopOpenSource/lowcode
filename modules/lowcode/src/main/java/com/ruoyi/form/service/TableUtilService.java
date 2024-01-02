package com.ruoyi.form.service;

import com.ruoyi.form.domain.FormObject;

public interface TableUtilService {
    /**
     * 插入列
     * @param sysFormObject
     */
    void insertAttributes(FormObject sysFormObject);

    /**
     * 修改列
     * @param formObject
     */
    void modiAttributes(FormObject formObject);

    /**
     * 创建表
     * @param tableName
     */
    void createTable(String tableName);

    /**
     * 根据表名删除表
     * @param tableName
     */
    void deleteTableByName(String tableName);

    /**
     * 校验主表中是否存在数据
     * @param tableName
     * @return
     */
    Boolean checkExitDataMainTable(String tableName);
}
