package com.ruoyi.form.service.impl;

import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.utils.Snowflake;
import com.ruoyi.dynamicform.constant.SystemConstant;
import com.ruoyi.form.domain.LayoutTable;
import com.ruoyi.form.domain.LayoutView;
import com.ruoyi.form.dto.LayoutTableDto;
import com.ruoyi.form.mapper.LayoutTableDao;
import com.ruoyi.form.service.LayoutTableService;
import com.ruoyi.system.api.model.LoginUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author
 *
 */
@Service("sysFormLayoutTableService")
@Log4j2
public class LayoutTableServiceImpl implements LayoutTableService {
    private static Snowflake snowflake = new Snowflake();

    @Autowired
	private LayoutTableDao tableDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
	public void insert(LayoutTableDto dto) {
	    Date now = new Date();
	    dto.setId(snowflake.nextId());
	    dto.setGmtCreate(now);
	    dto.setGmtModified(now);
		LoginUser loginUser= SecurityUtils.getLoginUser();
        dto.setCreateAccount(loginUser.getUsername());
		dto.setModifiedAccount(loginUser.getUsername());
		if (dto.getEnable() == null){
			dto.setEnable(SystemConstant.DEFUALT_0);
		}
        dto.setIsDefault(SystemConstant.DEFUALT_0);
		tableDao.insertDto(dto);

		Integer index = 0;
		for(String vId:dto.getViewIds()){
			dto.setViewId(vId);
			dto.setTableViewId(snowflake.nextId());
			dto.setIndex(index.toString());
			tableDao.insertLayoutTableViews(dto);
			index++;
		}
	}

	@Override
	public void updateByPK(LayoutTable dto) {
		LoginUser loginUser=SecurityUtils.getLoginUser();

	    Date now = new Date();
		dto.setGmtModified(now);

        dto.setModifiedAccount(loginUser.getUsername());

		tableDao.updateByPK(dto);
	}

	@Override
	public void updateSelectiveByPK(LayoutTable dto) {
		LoginUser loginUser=SecurityUtils.getLoginUser();

	    Date now = new Date();
		dto.setGmtModified(now);
        dto.setModifiedAccount(loginUser.getUsername());
		tableDao.updateSelectiveByPK(dto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
	public void updateSelectiveDtoByPK(LayoutTableDto dto) {
		LoginUser loginUser=SecurityUtils.getLoginUser();

		Date now = new Date();
		dto.setGmtModified(now);
		dto.setModifiedAccount(loginUser.getUsername());
		tableDao.updateSelectiveDtoByPK(dto);

		//删除已删除的视图表
		tableDao.deleteLayoutTableViewsByViewId(dto.getId());

		//新增编辑视图关联表
		Integer index = 0;
		for(String vId:dto.getViewIds()){
			dto.setViewId(vId);
			dto.setIndex(index.toString());
			dto.setTableViewId(snowflake.nextId());
			tableDao.insertLayoutTableViews(dto);

			index++;
		}

	}

	@Override
	public void deleteByPK(Long id) {
		tableDao.deleteByPK(id);
	}

	@Override
	public LayoutTable selectByPK(Long id) {
		return tableDao.selectByPK(id);
	}

	@Override
	public LayoutTableDto selectDtoByPK(Long id) {
		LayoutTableDto sysFormLayoutTableDto = tableDao.selectDtoByPK(id);
		List<String> strings = tableDao.selectRefViewIds(id);
		sysFormLayoutTableDto.setViewIds(strings);
		return sysFormLayoutTableDto;
	}

	@Override
	public List<LayoutTable> selectByDto(LayoutTable dto) {
		return tableDao.selectByDto(dto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
	public void enable(LayoutTable dto) {
		Date now = new Date();
		Long id=dto.getId();
		dto.setGmtModified(now);
		//禁用所有布局
		dto.setId(null);
		dto.setEnable(SystemConstant.DEFUALT_0);
		tableDao.enable(dto);
		//启用当前布局
		dto.setId(id);
		dto.setEnable(SystemConstant.DEFUALT_1);
		tableDao.enable(dto);
	}

	@Override
	public void delete(Long id) {
		tableDao.delete(id);
	}

	@Override
	public LayoutTable getEnableLayout(Long objectId) {
		LayoutTable dto = new LayoutTable();
		dto.setObjectId(objectId);
		dto.setEnable(SystemConstant.STR_YES);

		List<LayoutTable> layoutTables = tableDao.selectByDto(dto);

		if(layoutTables == null && layoutTables.size()==0){
			return null;
		}
		if (CollectionUtils.isEmpty(layoutTables)) {
			return null;
		}
		return layoutTables.get(0);
	}

	@Override
	public List<LayoutView> selectRefViews(Long tableId, Set<String> roleIds) {
		return tableDao.selectRefViews(tableId, roleIds);
	}

}
