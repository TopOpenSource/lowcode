package com.ruoyi.form.mapper;

import com.ruoyi.form.domain.FormObject;
import com.ruoyi.form.dto.ObjectDTO;
import com.ruoyi.form.mapper.base.ObjectBaseDao;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectDao extends ObjectBaseDao {
   List<FormObject> selectByDto(FormObject dto);
   Integer selectCountByDto(FormObject dto);
   List<ObjectDTO> listObjectDTO(ObjectDTO dto);

   @Update("update lowcode_object set state=#{state} where id=#{id}")
   void updateObjState(FormObject sysFormObject);
}
