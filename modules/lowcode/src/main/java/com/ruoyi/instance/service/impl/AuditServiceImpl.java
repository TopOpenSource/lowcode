package com.ruoyi.instance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.constant.FlowComment;
import com.ruoyi.common.core.exception.base.BaseException;
import com.ruoyi.common.dto.CommonDto;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.Snowflake;
import com.ruoyi.flow.domain.FlowInstance;
import com.ruoyi.flow.domain.FlowObject;
import com.ruoyi.flow.domain.FlowTask;
import com.ruoyi.flow.dto.FlowTaskDto;
import com.ruoyi.flow.mapper.FlowInstanceMapper;
import com.ruoyi.flow.mapper.FlowObjectMapper;
import com.ruoyi.flow.mapper.FlowTaskMapper;
import com.ruoyi.flow.service.AuditServiceTool;
import com.ruoyi.instance.mapper.AuditMapper;
import com.ruoyi.instance.service.AuditService;
import com.ruoyi.instance.service.InstanceService;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.DelegationState;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuditServiceImpl implements AuditService {
    @Autowired
    private AuditMapper auditMapper;
    @Autowired
    private FlowObjectMapper bpmnMapper;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FlowInstanceMapper flowInstanceMapper;
    @Autowired
    private FlowTaskMapper flowTaskMapper;
    @Autowired
    private InstanceService customerService;

    @Autowired
    private AuditServiceTool auditServiceTool;

    private static Snowflake snowflake = new Snowflake();

    @Override
    public List<FlowTaskDto> myTasks(FlowTaskDto flowTaskDto, String userName, List<Long> groupIds) {
        List<FlowTaskDto> tasks = auditMapper.getMyTask(flowTaskDto, userName, groupIds);
        return tasks;
    }

    @Override
    public FlowTaskDto selectMyTask(String userName, List<Long> groupIds,Long instId) {
        return auditMapper.selectMyTask(userName,groupIds,instId);
    }

    @Override
    public List<FlowTaskDto> getMyFinishedTask(FlowTaskDto flowTaskDto, String userName) {
        return auditMapper.getMyFinishedTask(flowTaskDto, userName);
    }

    @Override
    public List<FlowTaskDto> getUnSubmit(FlowTaskDto flowTaskDto, String userName) {
        return auditMapper.getUnSubmit(flowTaskDto, userName);
    }

    @Override
    public List<FlowTaskDto> getApplyTask(FlowTaskDto flowTaskDto, String username) {
        return auditMapper.getMyApplyTask(flowTaskDto, username);
    }

    @Override
    public List<FlowTask> getTaskHistory(Long instId) {
        FlowTask flowTask=new FlowTask();
        flowTask.setInstId(instId);
        return  flowTaskMapper.getTaskHistory(flowTask);
    }

    @Override
    public List<UserTask> findReturnTaskList(String taskId) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        return auditServiceTool.findReturnTaskList(task.getProcessDefinitionId(),task.getTaskDefinitionKey());
    }

    /**
     * submitState  代表提交状态 -1 不提交 0 存草稿  1 提交
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submit(CommonDto dto) {
        Long id = null;
        if (dto.get("id") != null) {
            id=dto.getAsLong("id");
            customerService.update(dto);
        } else {
            id=snowflake.nextId();
            dto.put("id", id);
            customerService.save(dto);
        }

        String applyUserName = dto.getAsString("modified_account");
        Long objectId = Long.parseLong(dto.get("objectId").toString());

        if(dto.getAsInteger("submitState")>-1){
            this.insertFlowInst(id, objectId, dto.getAsString("flowIncConf"), applyUserName, dto.getAsString("applyTitle"));
        }

        if(dto.getAsInteger("submitState")==1){
            ProcessInstance processInstance = this.startFlow(objectId, id, applyUserName);
            this.updateFlowInst(id, processInstance);
            //获取当前节点，如果为 taskApply，则自动提交
            List<Task> currentTasks = this.getCurrentTasks(processInstance.getProcessInstanceId());
            if(currentTasks!=null && currentTasks.size()==1){
                Task currentTask=currentTasks.get(0);
                if(currentTask.getTaskDefinitionKey().equals("taskApply")){
                    taskService.complete(currentTask.getId());
                    //插入任务记录
                    this.insertFlowTask(id, currentTask.getId(), currentTask.getName(), applyUserName, "申请",FlowComment.NORMAL.getType());
                }
            }

        }

        return dto.getAsLong("id");
    }

    //插入flow instance
    private void insertFlowInst(Long instId, Long objectId, String flowConfig, String applyUserName, String title) {
        FlowInstance flowInstanceByInstId = this.getFlowInstanceByInstId(instId);

        FlowInstance flowInstance = new FlowInstance(instId, objectId, instId, applyUserName, title);
        flowInstance.setFlowIncConfig(flowConfig);

        if(flowInstanceByInstId!=null){
            flowInstance.setUpdateTime(new Date());
            flowInstance.setUpdateBy(SecurityUtils.getUsername());
            flowInstanceMapper.updateFlowInst(flowInstance);
        }else{
            flowInstance.setCreateTime(new Date());
            flowInstance.setCreateBy(SecurityUtils.getUsername());
            flowInstanceMapper.insert(flowInstance);
        }
    }

    //更新流程信息
    private void updateFlowInst(Long instId, ProcessInstance processInstance) {
        FlowInstance update = new FlowInstance(instId, processInstance.getId(), processInstance.getDeploymentId(), processInstance.getProcessDefinitionId());
        update.setApplyTime(new Date());
        flowInstanceMapper.updateFlowInst(update);
    }

    //发起流程
    private ProcessInstance startFlow(Long objectId, Long instId, String applyUserName) {
        FlowObject flow = bpmnMapper.selectByFormObjectId(objectId);
        String processDefinitionId = flow.getProcessDefinitionId();

        Map<String, Object> variables = new HashMap<>();
        variables.put("objectId", objectId);
        variables.put("instanceId", instId);
        variables.put("applyUserName", applyUserName);
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinitionId, variables);
        return processInstance;
    }

    //插入处理记录
    private void insertFlowTask(Long instId,String taskId, String taskName, String taskAssignee, String comment,String checkState) {
        FlowTask flowTask = new FlowTask(snowflake.nextId(), instId, taskId, taskName, taskAssignee, comment);
        flowTask.setCreateTime(new Date());
        flowTask.setCheckState(checkState);
        flowTaskMapper.insert(flowTask);
    }

    /**
     * 审核-完成任务
     *
     * @param dto
     */
    @Override
    @Transactional
    public void complete(FlowTaskDto dto) {
        //查询任务
        Task task = taskService.createTaskQuery().taskId(dto.getTaskId()).singleResult();

        if (DelegationState.PENDING.equals(task.getDelegationState())) {
            taskService.resolveTask(dto.getTaskId());
        } else {
            //完成任务
            taskService.complete(dto.getTaskId());
        }

        //插入任务记录
        this.insertFlowTask(dto.getObjectInstanceId(), task.getId(), task.getName(), dto.getTaskAssignee(), dto.getComment(),FlowComment.NORMAL.getType());
    }

    /**
     * 驳回任务
     * @param
     */
    @Override
    public void taskReject(FlowTaskDto flowTaskVo) {
        if (taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult().isSuspended()) {
            throw new BaseException("任务处于挂起状态!");
        }
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult();
        // 任务驳回
        auditServiceTool.taskReject(task.getProcessDefinitionId(),task.getProcessInstanceId(),task.getTaskDefinitionKey(),flowTaskVo.getComment());
        //插入任务记录
        this.insertFlowTask(flowTaskVo.getObjectInstanceId(), task.getId(), task.getName(), flowTaskVo.getTaskAssignee(), flowTaskVo.getComment(), FlowComment.REJECT.getType());

    }

    @Override
    public void taskReturn(FlowTaskDto flowTaskVo) {
        if (taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult().isSuspended()) {
            throw new BaseException("任务处于挂起状态");
        }
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult();
        auditServiceTool.taskReturn(task.getProcessDefinitionId(),task.getProcessInstanceId(),task.getTaskDefinitionKey(),flowTaskVo.getTargetKey(),flowTaskVo.getComment());
        //插入任务记录
        this.insertFlowTask(flowTaskVo.getObjectInstanceId(), task.getId(), task.getName(), flowTaskVo.getTaskAssignee(), flowTaskVo.getComment(),FlowComment.REBACK.getType());
    }

    /**
     * 委派任务
     * @param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delegateTask(FlowTaskDto flowTaskVo) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult();
        this.insertFlowTask(flowTaskVo.getObjectInstanceId(), task.getId(), task.getName(), flowTaskVo.getTaskAssignee(), flowTaskVo.getComment(),FlowComment.DELEGATE.getType());

        taskService.delegateTask(flowTaskVo.getTaskId(), flowTaskVo.getTaskAssignee());
    }

    /**
     * 转办任务
     * @param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignTask(FlowTaskDto flowTaskVo) {
        // 当前任务 task
        Task task = taskService.createTaskQuery().taskId(flowTaskVo.getTaskId()).singleResult();
        this.insertFlowTask(flowTaskVo.getObjectInstanceId(), task.getId(), task.getName(), flowTaskVo.getTaskAssignee(), flowTaskVo.getComment(),FlowComment.ASSIGN.getType());

        taskService.setAssignee(flowTaskVo.getTaskId(), flowTaskVo.getTaskAssignee());
    }
    @Override
    public FlowInstance getFlowInstanceByInstId(Long instId){
        QueryWrapper<FlowInstance> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("object_instance_id",instId);
        FlowInstance flowInstance = flowInstanceMapper.selectOne(queryWrapper);
        return  flowInstance;
    }


    public List<Task> getCurrentTasks(String instId){
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(instId).list();
        return taskList;
    }
}
