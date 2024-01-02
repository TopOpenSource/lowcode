package com.ruoyi.form.service.impl;

import com.ruoyi.dynamicform.constant.SystemConstant;
import com.ruoyi.common.utils.Snowflake;
import com.ruoyi.form.domain.FormObjectAttribute;
import com.ruoyi.form.domain.LayoutForm;
import com.ruoyi.form.mapper.LayoutFormDao;
import com.ruoyi.form.mapper.ObjectAttributeDao;
import com.ruoyi.form.service.LayoutFormService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author
 */
@Service("sysFormLayoutFormService")
@Log4j2
public class LayoutFormServiceImpl implements LayoutFormService {
    private static Snowflake snowflake = new Snowflake();

    @Autowired
    private LayoutFormDao sysFormLayoutFormDao;
    @Autowired
    private ObjectAttributeDao sysFormObjectAttributeDao;


    @Override
    public void save(LayoutForm dto) {
        Date now = new Date();
        dto.setIsDefault(SystemConstant.DEFUALT_0);
        if (dto.getEnable() == null){
            dto.setEnable(SystemConstant.DEFUALT_0);
        }
        dto.setGmtCreate(now);
        dto.setGmtModified(now);
        sysFormLayoutFormDao.insert(dto);
    }

    @Override
    public void update(LayoutForm dto) {
        Date now = new Date();
        dto.setGmtModified(now);
        sysFormLayoutFormDao.update(dto);
    }



    @Override
    public List<LayoutForm> selectByDto(LayoutForm dto) {
        return sysFormLayoutFormDao.selectByDto(dto);
    }

    @Override
    public LayoutForm selectById(Long id) {
        return  sysFormLayoutFormDao.selectByPK(id);
    }

    /**
     * 启用
     * @param dto
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public void enable(LayoutForm dto) {
        Date now = new Date();
        Long id=dto.getId();
        dto.setGmtModified(now);
        //禁用所有布局
        dto.setId(null);
        dto.setEnable(SystemConstant.DEFUALT_0);
        sysFormLayoutFormDao.enable(dto);
        //启用当前布局
        dto.setId(id);
        dto.setEnable(SystemConstant.DEFUALT_1);
        sysFormLayoutFormDao.enable(dto);
    }

    @Override
    public void delete(Long id) {
        sysFormLayoutFormDao.delete(id);
    }

    @Override
    public LayoutForm getEnableForm(Long objectId, String type) {
        /**
         * 1. 根据 objectId，type查询启用的表单
         * 2. 查询所有属性放置Form4EditDTO中
         */
        LayoutForm enableForm = sysFormLayoutFormDao.getEnableForm(objectId, type);

        if (enableForm == null) {
            return null;
        }

        FormObjectAttribute attributeDto=new FormObjectAttribute();
        attributeDto.setObjectId(objectId);
        attributeDto.setEnable(SystemConstant.DEFUALT_1);
        enableForm.setAttributes(sysFormObjectAttributeDao.selectByDto(attributeDto));

        return enableForm;
    }

    @Override
    public boolean hasEnableForm(Long objectId, String type) {
        return sysFormLayoutFormDao.getEnableForm(objectId, type) == null ? false : true;
    }


}
