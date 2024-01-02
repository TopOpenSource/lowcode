package com.ruoyi.form.service;

import com.ruoyi.form.domain.LayoutForm;

import java.util.List;

/**
 *
 * @author
 *
 */
public interface LayoutFormService {
	void save(LayoutForm dto);

	void update(LayoutForm dto);

	List<LayoutForm> selectByDto(LayoutForm dto);

	LayoutForm selectById(Long id);

	void enable(LayoutForm dto);

	void delete(Long id);

	/**
	 * 获取启用的form
	 * @return
	 */
	LayoutForm getEnableForm(Long objectId, String type);

	boolean hasEnableForm(Long objectId, String type);

}
