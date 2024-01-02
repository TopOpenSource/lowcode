package com.ruoyi.dynamicform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.dynamicform.domain.InstRelation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 关系表
 */
@Repository
public interface InstRelationMapper extends BaseMapper<InstRelation> {

    @Delete("delete from lowcode_inst_relation where source_inst_id=#{instId} and source_attr_id=#{attrId}")
    void delByAttr(@Param("instId") Long instId,@Param("attrId") Long attrId);


    @Insert("<script>" +
            "insert into  lowcode_inst_relation \n" +
              "(id,source_inst_id,source_obj_id,source_attr_id,target_inst_id,target_obj_id) " +
            "values " +
            "<foreach collection=\"relations\" item=\"attr\"  separator=\",\" >" +
              "(#{attr.id}, #{attr.sInstId},#{attr.sObjId},#{attr.sAttrId},#{attr.tInstId},#{attr.tObjId})" +
            "</foreach>"+
            "</script>")
    void insertBatch(@Param("relations") List<InstRelation> relations);
}
