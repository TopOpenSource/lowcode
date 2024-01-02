package com.ruoyi.form.mapper;

import com.ruoyi.form.domain.FormObject;
import org.apache.ibatis.annotations.Param;

public interface TableUtilDao {

    void insertColumn(FormObject formObject);

    void modiColumn(FormObject formObject);

    /**
     * 创建主表
     * @param tableName
     */
    void createMainTable(@Param("tableName") String tableName);

    /**
     * 创建扩展表
     * @param tableName
     */
    void createExtTable(String tableName);

    void deleteTableByName(String tableName);

    /**
     * 查询主表是否存在数据
     * @param tableName
     * @return
     */
    long selectMainTableData(String tableName);
}
