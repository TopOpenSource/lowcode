package com.ruoyi.flow.listener;

import com.ruoyi.common.security.utils.SpringUtils;
import com.ruoyi.flow.service.WorkFlowService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;

/*
流程结束监听
 */
public class FlowEndListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        WorkFlowService workFlowService = SpringUtils.getBean("workFlowService");
        workFlowService.complateFlowState(delegateExecution.getProcessInstanceId());
    }
}
