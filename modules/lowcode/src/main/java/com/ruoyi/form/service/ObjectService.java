package com.ruoyi.form.service;

import com.ruoyi.form.domain.FormObject;
import com.ruoyi.form.dto.ObjectDTO;

import java.util.List;

/**
 * 
 * @author
 *
 */
public interface ObjectService {
	//void insert(SysFormObject dto);

	void updateByPK(FormObject dto);

	//void updateSelectiveByPK(SysFormObject dto);

	void deleteByPK(Long id);

	FormObject selectByPK(Long id);

	List<FormObject> selectByDto(FormObject dto);
	

    void insert(FormObject dto);

	void updateSelectiveByPK(FormObject dto);

	void deleteByObj(FormObject sysFormObject);

	List<ObjectDTO> listObjectDTO(ObjectDTO dto);

    void updateObjState(FormObject sysFormObject);
}
