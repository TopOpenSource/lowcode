package com.ruoyi.flow.service;

import com.ruoyi.flow.domain.FlowInstance;
import com.ruoyi.flow.domain.FlowObject;
import com.ruoyi.flow.dto.FlowViewerDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @program: RuoYi-Cloud-lowcode
 * @description: 流程定义
 * @author: liyh
 * @create: 2022-03-07 11:13
 **/
public interface WorkFlowService {
    /**
     * 根据文件内容部署流程
     */
    void addDeploymentByStr(String strBPM, Long objectId,String config);

    /**
     * 读取xml文件
     * @return
     */
    Map<String,String> readXmlStrByDefId(Long formObjectId) throws IOException;

    List<FlowViewerDto> getFlowViewer(Long insId);

    FlowObject  selectByFormObjectId(Long objectId);

    FlowInstance selectByFormInstId(Long instId);

    /**
     * 批量删除实例id  flow_inst  flow_task
     * @param ids
     */
    void delBatchByInstId(List<Long> ids);

    /**
     * 更新流程状态
     * @param procInstId
     */
    void updateFlowState(String taskName,String taskDefKey,String taskId,String procInstId,String proceDefId);

    /**
     * 完成流程
     * @param procInstId
     */
    void complateFlowState(String procInstId);
}
