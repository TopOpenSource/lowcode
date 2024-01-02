package com.ruoyi.form.service;

import com.ruoyi.form.domain.FormObjectAttribute;
import com.ruoyi.form.dto.ObjectAttributeDTO;
import com.ruoyi.form.domain.AttributeType;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 
 * @author
 *
 */
public interface ObjectAttributeService {
	void insert(FormObjectAttribute dto);

	void deleteByPK(Long id);

	FormObjectAttribute selectByPK(Long id);

	List<FormObjectAttribute> selectByDto(FormObjectAttribute dto);

	void updateName(FormObjectAttribute dto);

	/**
	 * 获取业务对象字段列表
	 *
	 * @param dto
	 * @return List<SysFormObjectAttributeDTO>
	 */
    List<ObjectAttributeDTO> listFormObjectAttribute(ObjectAttributeDTO dto);

	void deleteFormObjectAttribute(ObjectAttributeDTO dto);

	/**
	 * 移除/恢复 字段
	 *
	 * @param dto
	 */
	void toggleAttributeEnable(ObjectAttributeDTO dto);

	/**
	 * 获取字段类型基础模型数据
	 *
	 * @return List<SysFormAttributeType>
	 */
    List<AttributeType> listAttributeTypes();

	/**
	 * 新增字段
	 * @param dto
	 */
	void saveFormObjectAttribute(ObjectAttributeDTO dto);
	void saveFormObjectAttributeMX(List<ObjectAttributeDTO> list);

	/**
	 * 修改字段
	 * @param dto
	 */
	void updateFormObjectAttribute(ObjectAttributeDTO dto);

	/**
	 * 获取业务对象属性列表
	 * @param objectId
	 * @return
	 */
    List<FormObjectAttribute> getObjectAttributes(@NotNull Long objectId);

	void deleteAttr(ObjectAttributeDTO dto);
}
