package com.ruoyi.form.service;

import com.ruoyi.form.domain.LayoutView;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 
 * @author
 *
 */
public interface LayoutViewService {
	void insert(LayoutView dto);

	void updateByPK(LayoutView dto);

	void deleteByPK(Long id);

	LayoutView selectByPK(Long id);

	List<LayoutView> selectByDto(LayoutView dto);

	void enable(LayoutView dto);

	void delete(Long id);

	/**
	 * 获取默认的关联视图
	 * @param objectId
	 * @return
	 */
	 LayoutView selectDefaultRefView(Long objectId);

}
