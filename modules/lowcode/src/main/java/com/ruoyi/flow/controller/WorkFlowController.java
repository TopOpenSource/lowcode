package com.ruoyi.flow.controller;


import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.flow.domain.FlowObject;
import com.ruoyi.flow.dto.FlowModel;
import com.ruoyi.flow.service.WorkFlowService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program:
 * @description: 流程定义模块
 * @author: liyh
 * @create: 2022-03-04 13:55
 **/
@Slf4j
@RestController
@RequestMapping("/flow")
public class WorkFlowController {

    @Autowired
    private WorkFlowService workFlowService;
    @Autowired
    private RepositoryService repositoryService;


    /**
     * 根据xml文件内容创建流程
     *
     * @return
     */
    @PostMapping("/addDeploymentByStr")
    public AjaxResult addDeploymentByStr(@RequestBody FlowModel model) throws UnsupportedEncodingException {
        workFlowService.addDeploymentByStr(URLDecoder.decode(model.getStrBPM(), "UTF-8"), model.getFormObjectId(), model.getConfig());
        return AjaxResult.success();
    }

    /**
     *
     * @param formObjectId
     * @return
     * @throws IOException
     */
    @GetMapping("/readXmlStr/{formObjectId}")
    public AjaxResult readXmlStrByDefId(@PathVariable("formObjectId") Long formObjectId) throws IOException {
        return AjaxResult.success(workFlowService.readXmlStrByDefId(formObjectId));
    }


    /**
     * 生成流程图
     *
     */
    @RequestMapping("/flowViewer/{instId}")
    public AjaxResult getFlowViewer(@PathVariable("instId") Long instId) {
        return AjaxResult.success(workFlowService.getFlowViewer(instId));
    }


    /**
     * 获取流程实例信息
     * @param objectId
     * @return
     */
    @RequestMapping("/flowObject/{objectId}")
    public AjaxResult getFlowObject(@PathVariable("objectId") Long objectId) {
        FlowObject flow = workFlowService.selectByFormObjectId(objectId);
        return AjaxResult.success(flow);
    }

}
