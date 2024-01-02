package com.ruoyi.instance.service;

import com.ruoyi.common.dto.CommonDto;
import com.ruoyi.flow.domain.FlowInstance;
import com.ruoyi.flow.domain.FlowTask;
import com.ruoyi.flow.dto.FlowTaskDto;
import org.flowable.bpmn.model.UserTask;

import java.util.List;

public interface AuditService {

    /**
     * 我的待办
     * @param userName
     * @param groupId
     * @return
     */
    List<FlowTaskDto> myTasks(FlowTaskDto flowTaskDto, String userName, List<Long> groupId);

    /**
     * 检查是否我的待办
     * @param userName
     * @param groupIds
     * @param instId
     * @return
     */
    FlowTaskDto selectMyTask(String userName,List<Long> groupIds,Long instId);


    /**
     * 我参与的
     * @param userName
     * @return
     */
    List<FlowTaskDto> getMyFinishedTask(FlowTaskDto flowTaskDto, String userName);

    /**
     * 我的草稿
     * @param userName
     * @return
     */
    List<FlowTaskDto> getUnSubmit(FlowTaskDto flowTaskDto, String userName);

    List<FlowTask>  getTaskHistory(Long instId);

    /**
     * 获取所有可回退的节点
     *
     * @param taskId 任务ID
     * @return
     */
    List<UserTask> findReturnTaskList(String taskId);

    Long submit(CommonDto dto);

    /**
     * 完成任务 同意
     * @param dto
     */
    void complete(FlowTaskDto dto);

    /**
     * 退回任务
     * @param flowTaskVo
     */
    void taskReturn(FlowTaskDto flowTaskVo);

    /**
     * 驳回任务
     *
     * @param flowTaskVo
     */
    void taskReject(FlowTaskDto flowTaskVo);

    /**
     * 委派任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void delegateTask(FlowTaskDto flowTaskVo);

    /**
     * 转办任务
     *
     * @param flowTaskVo 请求实体参数
     */
    void assignTask(FlowTaskDto flowTaskVo);

    /**
     * 获取实例申请信息
     * @param instId
     * @return
     */
    FlowInstance getFlowInstanceByInstId(Long instId);

    /**
     * 查询我的申请
     * @param username
     * @return
     */
    List<FlowTaskDto> getApplyTask(FlowTaskDto flowTaskDto, String username);
}
