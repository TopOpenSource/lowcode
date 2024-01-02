package com.ruoyi.form.mapper;

import com.ruoyi.form.mapper.base.LayoutViewBaseDao;
import com.ruoyi.form.domain.LayoutView;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LayoutViewDao extends LayoutViewBaseDao {
   List<LayoutView> selectByDto(LayoutView dto);
   void enable(LayoutView dto);

   @Delete("delete from lowcode_layout_view where id=#{id}  and is_default='0' ;" +
           "delete from lowcode_layout_table_view where view_id = #{id}")
   void delete(@Param("id") Long id);

   List<LayoutView> selectDefaultLayoutViews(@Param("objectId") Long objectId);

   void deleteByObjectId(@Param("objectId") Long objectId);
}
