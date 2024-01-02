package com.ruoyi.form.mapper.base;


import com.ruoyi.form.domain.FormObjectAttribute;
import com.ruoyi.form.dto.FormObjectAttributeDto;
import com.ruoyi.form.dto.ObjectAttributeDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ObjectAttributeBaseDao {
   void  insert(FormObjectAttribute dto);
   void  deleteByPK(@Param("id") Long id);
   FormObjectAttribute selectByPK(@Param("id") Long id);
   void deleteById(@Param("id") Long id);

   List<String> canDel(ObjectAttributeDTO dto);

   void deleteByCol(ObjectAttributeDTO dto);

   List<FormObjectAttribute> selectByObjectId(@Param("objectId") Long objectId);


}
