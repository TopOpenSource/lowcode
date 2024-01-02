package com.ruoyi.flow.listener;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.dto.CommonDto;
import com.ruoyi.common.security.utils.SpringUtils;
import com.ruoyi.dynamicform.mapper.DynamicFormMapper;
import com.ruoyi.flow.domain.FlowInstance;
import com.ruoyi.flow.domain.FlowObject;
import com.ruoyi.flow.mapper.FlowInstanceMapper;
import com.ruoyi.flow.mapper.FlowObjectMapper;
import com.ruoyi.instance.service.AuditService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 执行发起其他流程 TODO
 */
public class SendProcessExc implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) {
        //processDefinitionId 查 workflow获取config， activityId 筛选config； processInstanceId 查workflow_instance， 创建新流程
        String processDefinitionId = delegateExecution.getProcessDefinitionId();
        String currentActivityId = delegateExecution.getCurrentActivityId();
        String processInstanceId = delegateExecution.getProcessInstanceId();

        FlowObjectMapper flowObjectMapper = SpringUtils.getBean(FlowObjectMapper.class);
        FlowObject flowObject = flowObjectMapper.getByProcDefId(processDefinitionId);

        String config = flowObject.getConfig();
        Long formObjectId = flowObject.getFormObjectId();
        JSONObject jsonObject = JSONObject.parseObject(config).getJSONObject(currentActivityId);
        Object refObjectId = jsonObject.get("refObjectId");

        FlowInstanceMapper flowInstanceMapper = SpringUtils.getBean(FlowInstanceMapper.class);
        FlowInstance flowInstance = flowInstanceMapper.findByProcInstId(processInstanceId);

        String comment = flowInstance.getComment();
        DynamicFormMapper dynamicFormMapper = SpringUtils.getBean(DynamicFormMapper.class);
        CommonDto dto = new CommonDto();
        Long flowInstanceId = flowInstance.getId();
        JSONObject attrsMatch = jsonObject.getJSONObject("attrsMatch");
        attrsMatch.forEach((key, value) -> {

            // 此处value格式为：{ "alias":null, "model":0, "value":null }
            // model：0 匹配项， 1 填写项
            JSONObject attrObj = (JSONObject) value;
            if (attrObj.get("model").equals(0)) {
                if (attrObj.get("alias")!=null) {
                    Object o = dynamicFormMapper.getValue((String) attrObj.get("alias"), "lowcode_table_" + formObjectId, flowInstanceId);
                    dto.put(key, o);
                }
            }
            if (attrObj.get("model").equals(1)) {
                if (attrObj.get("value")!=null) {
                    dto.put(key, attrObj.get("value"));
                }
            }
        });

        dto.put("modified_account", flowInstance.getCreateBy());
        dto.put("gmt_modified", new Date());
        dto.put("create_account", flowInstance.getCreateBy());
        dto.put("gmt_create", new Date());
        dto.put("objectId", refObjectId);
        dto.put("submitState", 1);
        dto.put("applyTitle", "流程" + comment + "发起的新流程");

        AuditService auditService = SpringUtils.getBean(AuditService.class);
        auditService.submit(dto);

    }
}
