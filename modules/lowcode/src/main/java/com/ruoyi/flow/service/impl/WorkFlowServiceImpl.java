package com.ruoyi.flow.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.dto.CommonDto;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.utils.Snowflake;
import com.ruoyi.dynamicform.service.DynamicFormService;
import com.ruoyi.flow.domain.FlowInstance;
import com.ruoyi.flow.domain.FlowObject;
import com.ruoyi.flow.domain.FlowTask;
import com.ruoyi.flow.dto.FlowNoticeInfo;
import com.ruoyi.flow.dto.FlowViewerDto;
import com.ruoyi.flow.dto.FlowConfigNotice;
import com.ruoyi.flow.mapper.FlowInstanceMapper;
import com.ruoyi.flow.mapper.FlowObjectMapper;
import com.ruoyi.flow.mapper.FlowTaskMapper;
import com.ruoyi.flow.notifier.SenderFactory;
import com.ruoyi.flow.service.WorkFlowService;
import com.ruoyi.form.domain.FormObjectAttribute;
import com.ruoyi.form.mapper.base.ObjectAttributeBaseDao;
import com.ruoyi.system.api.RemoteLowCodeService;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysDictData;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.api.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @program: RuoYi-Cloud-lowcode
 * @description:
 * @author: liyh
 * @create: 2022-03-07 11:15
 **/
@Slf4j
@Service("workFlowService")
public class WorkFlowServiceImpl implements WorkFlowService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private FlowObjectMapper bpmnMapper;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FlowInstanceMapper flowInstanceMapper;

    @Autowired
    private FlowTaskMapper flowTaskMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private SenderFactory senderFactory;

    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private ObjectAttributeBaseDao objectAttributeBaseDao;

    @Autowired
    private DynamicFormService dynamicFormService;

    @Autowired
    private RemoteLowCodeService remoteLowCodeService;

    private static Snowflake snowflake = new Snowflake();

    private final String suffixName = ".bpmn20.xml";

    /**
     * 根据xml文件内容部署流程
     *
     * @param strBPM
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addDeploymentByStr(String strBPM, Long objectId, String config) {
        // 部署成功
        Deployment deploy = repositoryService.createDeployment()
                .addString(objectId + suffixName, strBPM)
                .deploy();

        // 获取流程定义Id
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();

        // 根据workflowId修改对象
        FlowObject bpmnModelDomain = new FlowObject();
        bpmnModelDomain.setValue(objectId, processDefinition.getDeploymentId(), processDefinition.getId());
        bpmnModelDomain.setConfig(config);

        this.saveOrupdateWorkFlow(bpmnModelDomain);
    }

    /**
     * 读取xml文件
     *
     * @return
     */
    @Override
    public Map<String, String> readXmlStrByDefId(Long formObjectId) throws IOException {
        //查询对应的流程定义信息
        FlowObject flow = bpmnMapper.selectByFormObjectId(formObjectId);
        if (flow != null) {
            InputStream inputStream = repositoryService.getProcessModel(flow.getProcessDefinitionId());
            Map<String, String> result = new HashMap<>();
            result.put("xml", IOUtils.toString(inputStream, StandardCharsets.UTF_8));
            result.put("config", flow.getConfig());
            return result;
        } else {
            return null;
        }

    }


    private void saveOrupdateWorkFlow(FlowObject bpmnModelDomain) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (bpmnMapper.checkFlowExist(bpmnModelDomain.getFormObjectId()) > 0) {
            //编辑
            bpmnModelDomain.setUpdateData(String.valueOf(loginUser.getUserid()), new Date());
            bpmnMapper.updateFlowInfo(bpmnModelDomain);
        } else {
            //新增
            bpmnModelDomain.setInsertData(String.valueOf(loginUser.getUserid()), new Date());
            bpmnModelDomain.setId(snowflake.nextId());
            bpmnMapper.insert(bpmnModelDomain);
        }

    }


    @Override
    public List<FlowViewerDto> getFlowViewer(Long insId) {

        FlowInstance flowInstance = this.selectByFormInstId(insId);

        List<FlowViewerDto> flowViewerList = new ArrayList<>();
        List<HistoricActivityInstance> hisActIns = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(flowInstance.getProcInstId())
                .orderByHistoricActivityInstanceStartTime()
                .asc().list();

        for (HistoricActivityInstance activityInstance : hisActIns) {
            if (!"sequenceFlow".equals(activityInstance.getActivityType())) {
                FlowViewerDto flowViewerDto = new FlowViewerDto();
                flowViewerDto.setKey(activityInstance.getActivityId());
                // 根据流程节点处理时间校验改节点是否已完成
                flowViewerDto.setCompleted(!Objects.isNull(activityInstance.getEndTime()));
                flowViewerList.add(flowViewerDto);
            }
        }
        return flowViewerList;
    }

    @Override
    public FlowObject selectByFormObjectId(Long objectId) {
        FlowObject flow = bpmnMapper.selectByFormObjectId(objectId);
        return flow;
    }

    @Override
    public FlowInstance selectByFormInstId(Long instId) {
        QueryWrapper<FlowInstance> queryWrapper = new QueryWrapper();
        queryWrapper.eq("object_instance_id", instId);

        return flowInstanceMapper.selectOne(queryWrapper);
    }

    @Override
    public void delBatchByInstId(List<Long> ids) {
        QueryWrapper<FlowInstance> queryWrapper = new QueryWrapper();
        queryWrapper.in("object_instance_id", ids);

        List<FlowInstance> flowInstances = flowInstanceMapper.selectList(queryWrapper);
        //删除flowable
        for (FlowInstance flowInstance : flowInstances) {
            if (flowInstance.getProcInstId() != null) {
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(flowInstance.getProcInstId()).singleResult();
                if (null != processInstance) {
                    runtimeService.deleteProcessInstance(flowInstance.getProcInstId(), "流程实例删除");
                } else {
                    historyService.deleteHistoricProcessInstance(flowInstance.getProcInstId());
                }
            }
        }

        //删除inst关联表
        flowInstanceMapper.delete(queryWrapper);

        QueryWrapper<FlowTask> queryWrapperTask = new QueryWrapper();
        queryWrapperTask.in("inst_id", ids);
        //删除task关联表
        flowTaskMapper.delete(queryWrapperTask);
    }

    @Override
    public void updateFlowState(String taskName, String taskDefKey, String taskId, String procInstId,String procDefId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        /**
         * 1.查询当前环节是否与当前环节是否一致（多人）
         * 2.如果一致则将待办人拼接
         */
        QueryWrapper<FlowInstance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("proc_inst_id",procInstId);
        queryWrapper.eq("task_def_key",taskDefKey);
        FlowInstance flowInstance = flowInstanceMapper.selectOne(queryWrapper);

        String assignees=task.getAssignee();
        if(flowInstance!=null){
            assignees=assignees+","+flowInstance.getTaskAssignees();
        }

        UpdateWrapper<FlowInstance> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("task_name", taskName);
        updateWrapper.set("task_def_key", taskDefKey);
        updateWrapper.set("task_assignees", assignees);
        updateWrapper.eq("proc_inst_id", procInstId);
        flowInstanceMapper.update(null, updateWrapper);

        /**
         * TODO
         * 1. 查询通知配置  lowcode_workflow   proc_def_id=procDefId   flow_config-taskDefKey-notice
         * 2. 根据配置的通知给相关人员发送通知  assignees
         * 3. 发送内容： lowcode_workflow_instance  procInstId  您要一条审批： comment 创建人：apply_id
         */
        FlowObject flowObject = bpmnMapper.getByProcDefId(procDefId);
        String config = flowObject.getConfig();
        Long formObjectId = flowObject.getFormObjectId();
        // 获取当前信息
        JSONObject jsonObject = JSONObject.parseObject(config);
        JSONObject nowPoint = jsonObject.getJSONObject(taskDefKey);
        FlowConfigNotice flowConfigNotice = JSON.parseObject(JSON.toJSONString(nowPoint.get("notice")), FlowConfigNotice.class);
        if (flowConfigNotice != null) {
            if (flowConfigNotice.getNoticeEnable()) {
                FlowNoticeInfo flowNoticeInfo = getFlowNoticeInfo(assignees, procInstId, formObjectId);
                for (String s : flowConfigNotice.getCheckList()) {
                    senderFactory.getSender(s,flowNoticeInfo);
                }
            }
        }

    }

    public FlowNoticeInfo getFlowNoticeInfo(String assignees, String procInstId, Long formObjectId) {
        FlowNoticeInfo flowNoticeInfo = new FlowNoticeInfo();

        // 获取待办人
        String[] split = assignees.split(",");
        List<SysUser> sysUsers = new ArrayList<>();
        for (String s : split) {
            R<SysUser> userByName = remoteUserService.getUserByName(s, SecurityConstants.INNER);
            SysUser sysUser = userByName.getData();
            sysUsers.add(sysUser);
        }

        // 通知人
        flowNoticeInfo.setAssignees(sysUsers);
        // 拼装信息
        flowNoticeInfo.setMessage(mesCreator(procInstId, formObjectId));

        return flowNoticeInfo;
    }

    public String mesCreator(String procInstId, Long formObjectId){

        // 拼装通用信息
        FlowInstance flowInstance = flowInstanceMapper.findByProcInstId(procInstId);
        Long objectInstanceId = flowInstance.getObjectInstanceId();
        String mes = "您有一条新的审批：" + flowInstance.getComment();

        // 拼装表格信息
        // 获取属性 by formObjectId
        List<FormObjectAttribute> list = objectAttributeBaseDao.selectByObjectId(formObjectId);
        // 查询值 by formObjectId & objectInstanceId
        CommonDto resultDto = dynamicFormService.selectByPk(formObjectId, objectInstanceId, list);

        StringBuffer stringBuffer = new StringBuffer();
        list.forEach(item -> {
            String itemAlias = item.getAlias();
            Object value = resultDto.get(itemAlias);
            Object itemMes;
            if (item.getDictTypeCode()!=null && value!=null) {
                itemMes = dictDataDeal(item, (String) value);
            } else if (itemAlias.equals("create_account") || itemAlias.equals("modified_account")) {
                itemMes = nickNameDeal((String) value);
            } else if (itemAlias.equals("gmt_create") || itemAlias.equals("gmt_modified")) {
                itemMes = dateTimeDeal((LocalDateTime) value);
            } else {
                itemMes = value;
            }
            stringBuffer.append(item.getName()+"："+itemMes).append("\r\n");
        });

        String s = stringBuffer.toString();
        String aim = mes+"\r\n"+s;
        return aim;
    }

    @Override
    public void complateFlowState(String procInstId) {
        UpdateWrapper<FlowInstance> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("task_name", "完成");
        updateWrapper.set("task_def_key", "");
        updateWrapper.set("task_assignees", "");
        updateWrapper.set("is_complate","1");
        updateWrapper.eq("proc_inst_id", procInstId);
        flowInstanceMapper.update(null, updateWrapper);
    }

    public Object dictDataDeal(FormObjectAttribute item, String value) {
        SysDictData sysDictData = new SysDictData();
        sysDictData.setDictType(item.getDictTypeCode());
        sysDictData.setDictValue(value);
        return remoteLowCodeService.getValue(sysDictData).get("data");
    }

    public String nickNameDeal(String applyId) {
        R<SysUser> userByName = remoteUserService.getUserByName(applyId, SecurityConstants.INNER);
        SysUser data = userByName.getData();
        return data.getNickName();
    }

    public String dateTimeDeal(LocalDateTime date) {
        String format = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        return format;
    }

}
