package com.ruoyi.form.mapper;

import com.ruoyi.form.domain.FormObjectAttribute;
import com.ruoyi.form.mapper.base.ObjectAttributeBaseDao;
import com.ruoyi.form.dto.ObjectAttributeDTO;
import com.ruoyi.form.domain.AttributeType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectAttributeDao extends ObjectAttributeBaseDao {
   List<FormObjectAttribute> selectByDto(FormObjectAttribute dto);

   @Update("update lowcode_object_attribute set name=#{name} where id=#{id}  and is_default=0")
   void updateName(FormObjectAttribute dto);

   List<ObjectAttributeDTO> selectListByDTO(ObjectAttributeDTO dto);

   @Update("update lowcode_object_attribute set `enable`=#{enable} where id=#{id}  and is_default=0")
   void toggleAttributeEnable(ObjectAttributeDTO dto);

   /**
    * 获取字段类型基础模型数据
    * @return
    */
   @Results(id="SysFormAttributeTypeMap", value = {
           @Result(column = "db_type", property = "dbType"),
           @Result(column = "db_length", property = "dbLength"),
           @Result(column = "display_type", property = "displayType"),
           @Result(column = "display_regex", property = "displayRegex"),
           @Result(column = "display_fullline", property = "displayFullline"),
   })
   @Select("SELECT * FROM lowcode_attribute_type")
   List<AttributeType> selectTypeList();

   void insertOne(ObjectAttributeDTO dto);

   void updateOne(ObjectAttributeDTO dto);

   /**
    * 根据objectId删除
    * @param objectId
    */
    void deleteByObjectId(@Param("objectId") Long objectId);
}
