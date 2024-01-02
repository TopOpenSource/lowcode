package com.ruoyi.dynamicform.mapper;


import com.ruoyi.common.dto.CommonDto;
import com.ruoyi.dynamicform.dto.DynamicQueryDto;
import com.ruoyi.dynamicform.dto.TaskQueryDto;
import com.ruoyi.form.domain.FormObject;
import com.ruoyi.form.domain.FormObjectAttribute;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DynamicFormMapper {
    /**
     * 保存 主表信息
     * @param dto
     * @param attributes
     * @param formObject
     */
    @Insert("<script>" +
            "insert into ${formObject.tableName} " +
            "(`id`,`is_delete` " +
            "<foreach collection=\"attributes\" item=\"attr\" open=\"\" separator=\"\" close=\"\"> " +
            "       ,${'`'+attr.alias+'`'} " +
            "</foreach> " +
            ") " +
            "values " +
            "(#{dto.id},#{dto.isDelete}" +
            "<foreach collection=\"attributes\" item=\"attr\" open=\"\" separator=\"\" close=\"\"> " +
            "    ,#{${'dto.'+attr.alias}} " +
            "</foreach> " +
            ") " +
            "</script>")
    void save(@Param("dto") CommonDto dto, @Param("attributes") List<FormObjectAttribute> attributes, @Param("formObject") FormObject formObject);

    /**
     * 更新 主表信息
     * @param dto
     * @param attributes
     * @param formObject
     */
    @Update("<script>" +
            "update ${formObject.tableName} " +
            "<set> " +
            "<foreach collection=\"attributes\" item=\"attr\" open=\"\" separator=\",\" close=\"\"> " +
            "       ${'`'+attr.alias+'`'} = #{${'dto.'+attr.alias}} " +
            "</foreach> " +
            "</set> " +
            "<where> " +
            "id=#{dto.id} " +
            "</where> " +
            "</script>")
    void update(@Param("dto") CommonDto dto,@Param("attributes") List<FormObjectAttribute> attributes,@Param("formObject") FormObject formObject);


    @Select("<script>" +
            "select t1.id," +
            "<foreach collection=\"attributes\" item=\"attr\" open=\"\" separator=\",\" close=\"\"> " +
            "       ${'t1.'+attr.alias} as ${attr.alias} " +
            "</foreach> " +
            "from  ${formObject.tableName} t1 " +
            "<where> " +
            "t1.id=#{id} " +
            "</where> " +
            "</script>")
    CommonDto selectByPk(@Param("id")Long id,@Param("attributes") List<FormObjectAttribute> attributes,@Param("formObject") FormObject formObject);


    @Select("<script>" +
            "select count(*)  " +
            "from  ${formObject.tableName} t1 " +
            "<where>" +
            "   <if test='id!=null'>"+
            "     and t1.id != #{id}"+
            "   </if> "+
            "     and ${'t1.'+attr.alias} =#{value} " +
            "</where>"+
            "</script>")
    Integer checkRepeat(@Param("id")Long id,@Param("value")Object value,@Param("attr") FormObjectAttribute attribute,@Param("formObject") FormObject formObject);


    List<CommonDto> selectList(DynamicQueryDto dto);

    List<CommonDto> selectListWithTask(@Param("viewDto")DynamicQueryDto dto, @Param("taskDto")TaskQueryDto taskQueryDto);

    @Select("<script>" +
            "select count(*) " +
            "  from ${tableName} t1 " +
            "  <where> " +
            "   t1.`is_delete` = '0' " +
            "   <if test=\"queryWhere != null and queryWhere != ''\"> " +
            "    <![CDATA[  and ( ${queryWhere} )  ]]> " +
            "   </if> " +
            "   <if test=\"queryWhereAppend != null and queryWhereAppend != ''\"> " +
            "    <![CDATA[  and ( ${queryWhereAppend} )  ]]> " +
            "   </if> " +
            "  </where> " +
            "</script>")
    Integer selectCount(DynamicQueryDto dto);

    @Select("<script>" +
            " select " +
            "<foreach collection=\"attrs\" item=\"attrId\" separator=\",\" > " +
            "    ${'c'+attrId} " +
            "</foreach> " +
            " from ${tableName} " +
            " where id=#{instanceId}" +
            "</script>")
    CommonDto getAttrVal(@Param("tableName") String tableName,@Param("instanceId") Long instanceId, @Param("attrs") List<Long> attrs);



    void delBatch(@Param("tableName") String tableName,@Param("ids") List<Long> ids);

    @Select("<script>"+
            " select ${alias} from ${tableName} where id=#{instanceId} "+
            "</script>")
    Object getValue(@Param("alias") String alias, @Param("tableName") String tableName, @Param("instanceId") Long instanceId);


}
