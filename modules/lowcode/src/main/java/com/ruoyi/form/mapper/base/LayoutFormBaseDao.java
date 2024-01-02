package com.ruoyi.form.mapper.base;


import com.ruoyi.form.domain.LayoutForm;
import org.apache.ibatis.annotations.Param;


public interface LayoutFormBaseDao {
   void  insert(LayoutForm dto);
   void  update(LayoutForm dto);
   LayoutForm selectByPK(@Param("id") Long id);
}
