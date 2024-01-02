package com.ruoyi.flow.listener;

import com.ruoyi.common.security.utils.SpringUtils;
import com.ruoyi.flow.service.WorkFlowService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;

/**
 * 记录当前待办人的监听
 */
public class CreateAssignInfoListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        WorkFlowService workFlowService = SpringUtils.getBean("workFlowService");

        workFlowService.updateFlowState(delegateTask.getName(), delegateTask.getTaskDefinitionKey(), delegateTask.getId(), delegateTask.getProcessInstanceId(),delegateTask.getProcessDefinitionId());
    }
}
