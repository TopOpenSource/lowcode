package com.ruoyi.api;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.dto.CommonDto;
import com.ruoyi.flow.domain.FlowInstance;
import com.ruoyi.flow.domain.FlowTask;
import com.ruoyi.flow.service.WorkFlowService;
import com.ruoyi.instance.service.AuditService;
import com.ruoyi.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RequestMapping("/bug")
@RestController
public class BugController {

    @Autowired
    private AuditService auditService;
    @Autowired
    private WorkFlowService workFlowService;

    /**
     * 漏洞审批启动流程
     *
     * @param dto
     * @return
     */
    @PostMapping("/startFlow")
    public AjaxResult startFlow(@RequestBody CommonDto dto) {
        // todo 写死 后期集成更改
        LoginUser loginUser = new LoginUser();
        loginUser.setUsername("admin");

        dto.put("modified_account", loginUser.getUsername());
        dto.put("gmt_modified", new Date());
        dto.put("create_account", loginUser.getUsername());
        dto.put("gmt_create", new Date());
        dto.put("submitState", 1);

        Long id = auditService.submit(dto);
        return AjaxResult.success(id);
    }

    /**
     * 查询历史
     *
     * @param instId
     * @return
     */
    @GetMapping(value = "/taskHistory/{instId}")
    public AjaxResult selTaskHistory(@PathVariable("instId") Long instId) {
        List<FlowTask> tasks = auditService.getTaskHistory(instId);
        return AjaxResult.success(tasks);
    }


    /**
     * 实例状态查询
     * 1:已审核
     * 2：
     *
     * @param instId
     * @return
     */
    @GetMapping(value = "/instState/{instId}")
    public AjaxResult getInstState(@PathVariable("instId") Long instId) {
        FlowInstance instance = workFlowService.selectByFormInstId(instId);

        int resultCode = -1;
        if (instance != null) {
            resultCode = 0;     //审批中
            if (instance.getIsComplate()!=null && instance.getIsComplate().equals("1")) {
                resultCode = 1; //完成
            }
        }

        return AjaxResult.success(resultCode);
    }
}
