package com.ruoyi.form.service.impl;

import com.ruoyi.dynamicform.constant.SystemConstant;
import com.ruoyi.form.domain.LayoutView;
import com.ruoyi.form.mapper.LayoutViewDao;
import com.ruoyi.form.service.LayoutViewService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 
 * @author 
 *
 */
@Service("sysFormLayoutViewService")
@Log4j2
public class LayoutViewServiceImpl implements LayoutViewService {

    @Autowired
	private LayoutViewDao layoutViewDao;
    
	@Override
	public void insert(LayoutView dto) {
	    Date now = new Date();
	    dto.setGmtCreate(now);
		dto.setGmtModified(now);
		//dto.setEnable("1");
		layoutViewDao.insert(dto);
	}

	@Override
	public void updateByPK(LayoutView dto) {
	    Date now = new Date();
		dto.setGmtModified(now);
		layoutViewDao.updateSelectiveByPK(dto);
	}

	@Override
	public void deleteByPK(Long id) {
		layoutViewDao.deleteByPK(id);
	}

	@Override
	public LayoutView selectByPK(Long id) {
		return layoutViewDao.selectByPK(id);
	}

	@Override
	public List<LayoutView> selectByDto(LayoutView dto) {
		return layoutViewDao.selectByDto(dto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
	public void enable(LayoutView dto) {
		Date now = new Date();
		Long id=dto.getId();
		dto.setGmtModified(now);
		//禁用所有布局"-1"
		dto.setId(null);
		dto.setEnable(SystemConstant.DEFUALT_0);
		layoutViewDao.enable(dto);
		//启用当前布局
		dto.setId(id);
		dto.setEnable(SystemConstant.DEFUALT_1);
		layoutViewDao.enable(dto);
	}

	@Override
	public void delete(Long id) {
		layoutViewDao.delete(id);
	}

	@Override
	public LayoutView selectDefaultRefView(Long objectId) {
		LayoutView dto=new LayoutView();

		dto.setEnable("1");
		dto.setObjectId(objectId);
		dto.setType("-1");

		List<LayoutView> layoutViews = layoutViewDao.selectByDto(dto);
		if(layoutViews.size()>0){
			return layoutViews.get(0);
		}else{
			return null;
		}
	}

}
