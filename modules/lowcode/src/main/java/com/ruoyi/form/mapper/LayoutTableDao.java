package com.ruoyi.form.mapper;

import com.ruoyi.form.domain.LayoutView;
import com.ruoyi.form.mapper.base.LayoutTableBaseDao;
import com.ruoyi.form.dto.LayoutTableDto;
import com.ruoyi.form.dto.Table4ListDTO;
import com.ruoyi.form.domain.LayoutTable;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface LayoutTableDao extends LayoutTableBaseDao {
   List<LayoutTable> selectByDto(LayoutTable dto);

   void enable(LayoutTable dto);

   void insertDto(LayoutTableDto dto);

   void  updateSelectiveDtoByPK(LayoutTableDto dto);

   void insertLayoutTableViews(LayoutTableDto dto);

   void updateLayoutTableViews(LayoutTableDto dto);

   void deleteLayoutTableViewsByViewId(@Param("tableId") Long tableId);

   List<String> selectRefViewIds(@Param("tableId")Long tableId);

   List<LayoutView> selectRefViews(@Param("tableId")Long tableId,@Param("roleIds") Set<String> roleIds);

   @Delete("delete from lowcode_layout_table where id=#{id}  and is_default='0' ;" +
           "delete from lowcode_layout_table_view where table_id=#{id} ")
   void delete(@Param("id") Long id);

   Table4ListDTO getEnableTable( @Param("objectId") Long objectId);

   LayoutTableDto selectDtoByPK(@Param("id") Long id);

   void deleteByObjectId(@Param("objectId") Long objectId);

   void deleteViewByObjectId(@Param("objectId") Long objectId);
}
