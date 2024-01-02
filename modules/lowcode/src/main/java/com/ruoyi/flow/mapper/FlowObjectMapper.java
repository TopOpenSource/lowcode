package com.ruoyi.flow.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.flow.domain.FlowObject;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowObjectMapper extends BaseMapper<FlowObject> {

    /**
     * 更新
     * @param dto
     * @return
     */
    @Update("update lowcode_workflow set deploy_id=#{deploymentId}, proc_def_id=#{processDefinitionId},flow_config=#{config} where form_object_id=#{formObjectId}")
    int updateFlowInfo(FlowObject dto);

    /**
     * 检查worflow是否存在
     */
    @Select("select count(*) from lowcode_workflow where form_object_id=#{formObjectId}")
    int checkFlowExist(@Param("formObjectId") Long formObjectId);


    /**
     * 根据ObjectId查询流程信息
     * @param objectId
     * @return
     */
    FlowObject selectByFormObjectId(@Param("formObjectId") Long objectId);

    /**
     * 根据objectId删除流程信息
     * @param objectId
     */
    @Delete("delete from lowcode_workflow where form_object_id=#{formObjectId}")
    void deleteByFormObjectId(@Param("formObjectId") Long objectId);

    FlowObject getByProcDefId(@Param("processDefinitionId") String proceDefId);

}
