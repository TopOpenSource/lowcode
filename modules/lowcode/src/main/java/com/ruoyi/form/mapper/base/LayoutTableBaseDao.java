package com.ruoyi.form.mapper.base;


import com.ruoyi.form.domain.LayoutTable;
import org.apache.ibatis.annotations.Param;


public interface LayoutTableBaseDao {
   void  insert(LayoutTable dto);
   void  updateByPK(LayoutTable dto);
   void  updateSelectiveByPK(LayoutTable dto);
   void  deleteByPK(@Param("id") Long id);
   LayoutTable selectByPK(@Param("id") Long id);
}
