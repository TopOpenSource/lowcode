package com.ruoyi.instance.controller;

import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.dto.CommonDto;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.flow.domain.FlowInstance;
import com.ruoyi.flow.domain.FlowTask;
import com.ruoyi.flow.dto.FlowTaskDto;
import com.ruoyi.instance.service.AuditService;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysUserRoleDto;
import com.ruoyi.system.api.model.LoginUser;
import lombok.extern.log4j.Log4j2;
import org.flowable.bpmn.model.UserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/audit")
@Log4j2
public class AuditController extends BaseController {

    @Autowired
    private AuditService auditService;

    @Autowired
    private RemoteUserService remoteUserService;

    /**
     * 我的待办
     * @return
     */
    @GetMapping("/myTasks")
    public TableDataInfo myTasks(FlowTaskDto flowTaskDto) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        R<List<Long>> roleIds = remoteUserService.selectRoleListByUserId(new SysUserRoleDto(loginUser.getUserid()), SecurityConstants.INNER);
        startPage();
        List<FlowTaskDto> flowTaskDtos = auditService.myTasks(flowTaskDto, loginUser.getUsername(), roleIds.getData());
        return getDataTable(flowTaskDtos);
    }

    /**
     * 查询该实例是否为登录人的
     * @return
     */
    @GetMapping("/selectMyTask/{instId}")
    public AjaxResult selectMyTask(@PathVariable("instId") Long instId) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        R<List<Long>> roleIds = remoteUserService.selectRoleListByUserId(new SysUserRoleDto(loginUser.getUserid()), SecurityConstants.INNER);
        FlowTaskDto result=auditService.selectMyTask(loginUser.getUsername(),roleIds.getData(),instId);
        return AjaxResult.success(result);
    }

    /**
     * 我参与过的
     * @return
     */
    @GetMapping("/myFinishedTask")
    public TableDataInfo myFinishedTask(FlowTaskDto flowTaskDto) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        startPage();
        List<FlowTaskDto> flowTaskDtos = auditService.getMyFinishedTask(flowTaskDto, loginUser.getUsername());
        return getDataTable(flowTaskDtos);
    }

    /**
     * 未提交的
     * @return
     */
    @GetMapping("/myUnSubmitTask")
    public TableDataInfo myUnSubmitTask(FlowTaskDto flowTaskDto) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        startPage();
        List<FlowTaskDto> flowTaskDtos = auditService.getUnSubmit(flowTaskDto, loginUser.getUsername());
        return getDataTable(flowTaskDtos);
    }

    /**
     * 查询我的申请
     * @return
     */
    @GetMapping("/myApplyTask")
    public TableDataInfo myApplyTask(FlowTaskDto flowTaskDto) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        startPage();
        List<FlowTaskDto> flowTaskDtos =auditService.getApplyTask(flowTaskDto, loginUser.getUsername());
        return getDataTable(flowTaskDtos);
    }

    /**
     * 回退 节点
     * @param taskId
     * @return
     */
    @GetMapping("/findReturnTaskList/{taskId}")
    public List<UserTask> findReturnTaskList(@PathVariable("taskId") String taskId){
        return auditService.findReturnTaskList(taskId);
    }

    /**
     * 提交任务
     *
     * @param dto
     * @return
     */
    @PostMapping(value = "/submit")
    public AjaxResult submit(@RequestBody CommonDto dto) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (dto.get("id") != null) {
            dto.put("modified_account", loginUser.getUsername());
            dto.put("gmt_modified", new Date());
        } else {
            dto.put("modified_account", loginUser.getUsername());
            dto.put("gmt_modified", new Date());
            dto.put("create_account", loginUser.getUsername());
            dto.put("gmt_create", new Date());
        }
        Long id= auditService.submit(dto);
        return AjaxResult.success(id);
    }

    /**
     * 完成
     * @param dto
     * @return
     */
    @PostMapping(value = "/complete")
    public AjaxResult check(@RequestBody FlowTaskDto dto) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        dto.setTaskAssignee(String.valueOf(loginUser.getUsername()));
        auditService.complete(dto);
        return AjaxResult.success();
    }

    /**
     * 驳回
     * @param dto
     * @return
     */
    @PostMapping(value = "/taskReject")
    public AjaxResult taskReject(@RequestBody FlowTaskDto dto) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        dto.setTaskAssignee(loginUser.getUsername());
        auditService.taskReject(dto);
        return AjaxResult.success();
    }

    /**
     * 委派
     * @param dto
     * @return
     */
    @PostMapping(value = "/taskDelegate")
    public AjaxResult taskDelegate(@RequestBody FlowTaskDto dto) {
        auditService.delegateTask(dto);
        return AjaxResult.success();
    }

    /**
     * 转办
     * @param dto
     * @return
     */
    @PostMapping(value = "/taskAssign")
    public AjaxResult taskAssign(@RequestBody FlowTaskDto dto) {
        auditService.assignTask(dto);
        return AjaxResult.success();
    }

    /**
     * 退回 至某一节点
     * @param dto
     * @return
     */
    @PostMapping(value = "/taskReturn")
    public AjaxResult taskReturn(@RequestBody FlowTaskDto dto) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        dto.setTaskAssignee(loginUser.getUsername());
        auditService.taskReturn(dto);
        return AjaxResult.success();
    }

    /**
     * 审批历史
     * @param instId
     * @return
     */
    @GetMapping(value="taskHistory/{instId}")
    public  AjaxResult selTaskHistory(@PathVariable("instId") Long instId){
        List<FlowTask> tasks = auditService.getTaskHistory(instId);
        return AjaxResult.success(tasks);
    }

    @RequestMapping("selFlowInst/{instId}")
    public AjaxResult selFlowInst(@PathVariable("instId") Long instId){
        FlowInstance flowInst = auditService.getFlowInstanceByInstId(instId);
        return AjaxResult.success(flowInst);
    }

}
