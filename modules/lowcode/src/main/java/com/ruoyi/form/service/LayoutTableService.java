package com.ruoyi.form.service;

import com.ruoyi.form.domain.LayoutView;
import com.ruoyi.form.dto.LayoutTableDto;
import com.ruoyi.form.domain.LayoutTable;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author
 *
 */
public interface LayoutTableService {
	void insert(LayoutTableDto dto);

	void updateByPK(LayoutTable dto);

	void updateSelectiveByPK(LayoutTable dto);

	void updateSelectiveDtoByPK(LayoutTableDto dto);

	void deleteByPK(Long id);

	LayoutTable selectByPK(Long id);

	LayoutTableDto selectDtoByPK(Long id);

	List<LayoutTable> selectByDto(LayoutTable dto);

	void enable(LayoutTable dto);

	void delete(Long id);

	/**
	 * 获取可用的列表布局
	 * @param objectId
	 * @return
	 */
    LayoutTable getEnableLayout(@NotNull Long objectId);

	/**
	 * 获取关联的视图id
	 * @param tableId
	 * @return
	 */
	List<LayoutView> selectRefViews(Long tableId, Set<String> roleIds);
}
