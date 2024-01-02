package com.ruoyi.form.mapper;

import com.ruoyi.form.mapper.base.LayoutFormBaseDao;
import com.ruoyi.form.domain.LayoutForm;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LayoutFormDao extends LayoutFormBaseDao {
   List<LayoutForm> selectByDto(LayoutForm dto);
   void enable(LayoutForm dto);

   @Delete("delete from lowcode_layout_form where id=#{id}  and is_default='0'")
   void delete(@Param("id") Long id);

   @Select("select name,content from lowcode_layout_form where  object_id=#{objectId} and type=#{type}  and is_delete='0'  and enable='1'")
   LayoutForm getEnableForm(@Param("objectId")Long objectId, @Param("type")String type);

   LayoutForm selectFormDetailByObjectId(@Param("objectId") Long objectId, @Param("type") String type);

    void deleteByObjectId(@Param("objectId") Long objectId);
}
