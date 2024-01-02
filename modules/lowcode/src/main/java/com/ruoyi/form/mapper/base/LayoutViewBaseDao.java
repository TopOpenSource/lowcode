package com.ruoyi.form.mapper.base;


import com.ruoyi.form.domain.LayoutView;
import org.apache.ibatis.annotations.Param;


public interface LayoutViewBaseDao {
   void  insert(LayoutView dto);
   void  updateSelectiveByPK(LayoutView dto);
   void  deleteByPK(@Param("id") Long id);
   LayoutView selectByPK(@Param("id") Long id);
   
}
