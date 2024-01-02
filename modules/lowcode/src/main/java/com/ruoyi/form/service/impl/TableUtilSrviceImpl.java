package com.ruoyi.form.service.impl;

import com.ruoyi.form.domain.FormObject;
import com.ruoyi.form.mapper.TableUtilDao;
import com.ruoyi.form.service.TableUtilService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("tableUtilService")
@Log4j2
public class TableUtilSrviceImpl implements TableUtilService {
    @Autowired
    private TableUtilDao tableUtilDao;

    /**
     * 插入列
     *
     * @param sysFormObject
     */
    @Override
    @Transactional
    public void insertAttributes(FormObject sysFormObject) {
        tableUtilDao.insertColumn(sysFormObject);
    }

    @Override
    public void modiAttributes(FormObject formObject) {
        tableUtilDao.modiColumn(formObject);
    }

    @Override
    public void createTable(String tableName) {
        tableUtilDao.createMainTable(tableName);
    }

    @Override
    public void deleteTableByName(String tableName) {
        tableUtilDao.deleteTableByName(tableName);
    }

    @Override
    public Boolean checkExitDataMainTable(String tableName) {
        long count = tableUtilDao.selectMainTableData(tableName);
        return count > 0;
    }
}

