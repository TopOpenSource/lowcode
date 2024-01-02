package com.ruoyi.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.flow.domain.FlowInstance;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowInstanceMapper extends BaseMapper<FlowInstance> {
    void updateFlowInst(FlowInstance flowInstance);

    @Select("select * from lowcode_workflow_instance where proc_inst_id = #{procInstId}")
    FlowInstance findByProcInstId(@Param("procInstId") String procInstId);
}
