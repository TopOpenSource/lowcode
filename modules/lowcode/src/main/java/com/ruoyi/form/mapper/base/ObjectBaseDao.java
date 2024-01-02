package com.ruoyi.form.mapper.base;


import com.ruoyi.form.domain.FormObject;
import org.apache.ibatis.annotations.Param;


public interface ObjectBaseDao {
   void  insert(FormObject dto);
   void  updateByPK(FormObject dto);
   void  updateSelectiveByPK(FormObject dto);
   void  deleteByPK(@Param("id") Long id);
   FormObject selectByPK(@Param("id") Long id);
   
}
