package com.ruoyi.form.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.utils.Snowflake;
import com.ruoyi.dynamicform.constant.SystemConstant;
import com.ruoyi.form.domain.AttributeType;
import com.ruoyi.form.domain.FormObject;
import com.ruoyi.form.domain.FormObjectAttribute;
import com.ruoyi.form.dto.ObjectAttributeDTO;
import com.ruoyi.form.mapper.ObjectAttributeDao;
import com.ruoyi.form.service.ObjectAttributeService;
import com.ruoyi.form.service.ObjectService;
import com.ruoyi.form.service.TableUtilService;
import com.ruoyi.system.api.model.LoginUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author 
 *
 */
@Service("sysFormObjectAttributeService")
@Log4j2
public class ObjectAttributeServiceImpl implements ObjectAttributeService {
    private static Snowflake snowflake = new Snowflake();

    private final static String COL_PRE = "c";  // 自定义字段前缀

    @Autowired
	private ObjectAttributeDao sysFormObjectAttributeDao;

    @Autowired
	private TableUtilService tableUtilService;

    @Autowired
	private ObjectService sysFormObjectService;
    
	@Override
	public void insert(FormObjectAttribute dto) {
		LoginUser loginUser= SecurityUtils.getLoginUser();
	    Date now = new Date();
	    dto.setId(snowflake.nextId());
	    dto.setGmtCreate(now);
        dto.setCreateAccount(loginUser.getUsername());
		sysFormObjectAttributeDao.insert(dto);
	}

	@Override
	public void deleteByPK(Long id) {
		sysFormObjectAttributeDao.deleteByPK(id);
	}

	@Override
	public FormObjectAttribute selectByPK(Long id) {
		return sysFormObjectAttributeDao.selectByPK(id);
	}

	@Override
	public List<FormObjectAttribute> selectByDto(FormObjectAttribute dto) {
		return sysFormObjectAttributeDao.selectByDto(dto);
	}

	@Override
	public void updateName(FormObjectAttribute dto) {
		sysFormObjectAttributeDao.updateName(dto);
	}

	@Override
	public List<ObjectAttributeDTO> listFormObjectAttribute(ObjectAttributeDTO dto) {
		List<ObjectAttributeDTO> list = sysFormObjectAttributeDao.selectListByDTO(dto);
		if(ObjectUtil.isEmpty(list)){
			return new ArrayList<>();
		}
		return list;
	}

	/**
	 * 删除字段(假删)
	 * 1. 修改字段删除状态
	 * 2. 调整对应表
	 * @param dto
	 */
	@Override
	public void deleteFormObjectAttribute(ObjectAttributeDTO dto) {
		// 1. 修改字段删除状态
		sysFormObjectAttributeDao.deleteByPK(dto.getId());
		// 2. 调整对应表
	}

	/**
	 * 启用/禁用字段
	 * @param dto
	 */
	@Override
	public void toggleAttributeEnable(ObjectAttributeDTO dto) {
		sysFormObjectAttributeDao.toggleAttributeEnable(dto);

		// 调整布局
	}

	/**
	 * 获取所有字段类型
	 *
	 * @return
	 */
	@Override
	public List<AttributeType> listAttributeTypes() {
		List<AttributeType> list = sysFormObjectAttributeDao.selectTypeList();
		if(ObjectUtil.isEmpty(list)){
			return new ArrayList<>();
		}
		return list;
	}

	/**
	 * 新增字段
	 *
	 * @param dto
	 */
	@Transactional
	@Override
	public void saveFormObjectAttribute(ObjectAttributeDTO dto) {
		dto.setGmtCreate(new Date());
		dto.setIsDelete(SystemConstant.DELETE_0);
		dto.setEnable(SystemConstant.STR_YES);
//		dto.setDisplayFullline(SystemConstant.STR_NO);
		dto.setIsDefault(SystemConstant.STR_NO);
		dto.setAlias(COL_PRE+dto.getId());  // 不可修改
		sysFormObjectAttributeDao.insertOne(dto);

		// 调整数据库表  新增字段
		this.addColumn(dto);
	}

	@Override
	public void saveFormObjectAttributeMX(List<ObjectAttributeDTO> list){
		for (ObjectAttributeDTO dto : list) {
			dto.setId(snowflake.nextId());
			saveFormObjectAttribute(dto);
		}
	}

	private void addColumn(ObjectAttributeDTO dto) {
		FormObject formObject = sysFormObjectService.selectByPK(dto.getObjectId());
		formObject.setAttrs(new ArrayList<>());
		FormObjectAttribute attr = new FormObjectAttribute();
		BeanUtils.copyProperties(dto, attr);
		formObject.getAttrs().add(attr);

		tableUtilService.insertAttributes(formObject);
	}

	/**
	 * 编辑字段
	 *
	 * @param dto
	 */
	@Override
	public void updateFormObjectAttribute(ObjectAttributeDTO dto) {
		dto.setGmtModified(new Date());
		dto.setIsDelete(SystemConstant.DELETE_0);

		sysFormObjectAttributeDao.updateOne(dto);

		// 调整数据库表  修改字段
//		this.modiColumn(dto);
	}

	private void modiColumn(ObjectAttributeDTO dto) {
		FormObject formObject = sysFormObjectService.selectByPK(dto.getObjectId());
		formObject.setAttrs(new ArrayList<>());
		FormObjectAttribute attr = new FormObjectAttribute();
		BeanUtils.copyProperties(dto, attr);
		formObject.getAttrs().add(attr);

		tableUtilService.modiAttributes(formObject);
	}

	/**
	 * 获取业务对象字段
	 * @param objectId
	 * @return
	 */
	@Override
	public List<FormObjectAttribute> getObjectAttributes(Long objectId) {
		FormObjectAttribute dto = new FormObjectAttribute();
		dto.setObjectId(objectId);
//		dto.setEnable(SystemConstant.STR_YES);
		List<FormObjectAttribute> list = sysFormObjectAttributeDao.selectByDto(dto);
		if(list == null){
			return new ArrayList<>();
		}
		return list;
	}

	@Override
	@Transactional
	public void deleteAttr(ObjectAttributeDTO dto){
		sysFormObjectAttributeDao.deleteById(dto.getId());

		FormObject formObject = sysFormObjectService.selectByPK(dto.getObjectId());
		dto.setTableName(formObject.getTableName());
		List<String> list = sysFormObjectAttributeDao.canDel(dto);
		if (list == null || list.isEmpty()) {
			sysFormObjectAttributeDao.deleteByCol(dto);
		}
	}

}
