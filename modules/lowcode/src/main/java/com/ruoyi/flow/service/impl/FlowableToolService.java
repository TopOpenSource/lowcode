package com.ruoyi.flow.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.dto.CommonDto;
import com.ruoyi.common.utils.StringUtils_;
import com.ruoyi.dynamicform.dto.DynamicQueryDto;
import com.ruoyi.dynamicform.dto.Query;
import com.ruoyi.dynamicform.mapper.DynamicFormMapper;
import com.ruoyi.flow.domain.FlowInstance;
import com.ruoyi.flow.domain.FlowObject;
import com.ruoyi.flow.mapper.FlowInstanceMapper;
import com.ruoyi.flow.mapper.FlowObjectMapper;
import com.ruoyi.form.domain.FormObject;
import com.ruoyi.form.service.ObjectService;
import com.ruoyi.instance.service.InstanceService;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysUserRoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author cheng
 */
@Service("flowableToolService")
public class FlowableToolService {

    @Autowired
    private RemoteUserService remoteUserService;
    @Autowired
    private FlowInstanceMapper flowInstanceMapper;
    @Autowired
    private InstanceService instanceService;
    @Autowired
    private ObjectService objectService;
    @Autowired
    private FlowObjectMapper flowMapper;
    @Autowired
    private DynamicFormMapper dynamicFormMapper;

    public List<String> getByUsers(String userIds) {
        return Arrays.asList(userIds.split(","));
    }

    /**
     * 查询角色下的所有人员
     *
     * @param roles
     * @return
     */
    public List<String> getByRoles(String roles) {
        R<List<String>> userIds = remoteUserService.selectUserByRoles(new SysUserRoleDto(Arrays.asList(roles.split(","))), SecurityConstants.INNER);

        return userIds.getData();
    }

    /**
     * 查询组织下的leader
     *
     * @param applyUserName
     * @return
     */
    public List<String> getByLeader(String applyUserName) {
        List<String> users = new ArrayList<>();
        R<String> userId = remoteUserService.selectLeader(new SysUserRoleDto(applyUserName), SecurityConstants.INNER);
        users.add(userId.getData());
        return users;
    }


    /**
     * 发起人自选   查询用户配置的元素待办人
     *
     * @param instanceid
     * @param elementId
     * @return
     */
    public List<String> getByDiy(String instanceid, String elementId) {

        FlowInstance flowInstance = flowInstanceMapper.selectById(instanceid);
        Map<String, Map<String, JSONArray>> parse = (Map<String, Map<String,JSONArray>>) JSON.parse(flowInstance.getFlowIncConfig());

        return  parse.get(elementId).get("assignee").toJavaList(String.class);
    }

    /**
     * 页面元素
     * @param objectId
     * @param instanceId
     * @param attrs
     * @return
     */
    public List<String> getByAttrs(String objectId, String instanceId, String attrs) {
        List<String> attrIds = StringUtils_.parseString2ArrayList(attrs, ",");
        List<Long> attrIds2 = attrIds.stream().map(attrId -> {
            return Long.valueOf(attrId);
        }).collect(Collectors.toList());

        CommonDto attrVal = instanceService.getAttrVal(Long.valueOf(objectId), Long.valueOf(instanceId), attrIds2);

        List<String> userIds = new ArrayList<>();
        for (String attrId : attrIds) {
            String users = attrVal.getAsString("c" + attrId);
            if (!StringUtils_.isEmpty(users)) {
                userIds.addAll(StringUtils_.parseString2ArrayList(users, ","));
            }
        }
        return userIds;
    }

    /**
     * 判断条件是否符合
     */
    public boolean flowValid(String objectId, String instanceId, String elementId){

        //查询业务对象
        FormObject formObject = objectService.selectByPK(Long.valueOf(objectId));

        //查询配置的 条件
        FlowObject flow =flowMapper.selectByFormObjectId(Long.valueOf(objectId));

        Map<String,JSONObject> flowConfig = JSONObject.parseObject(flow.getConfig(), Map.class);

        String result=flowConfig.get(elementId).get("condition").toString();
        Query query = JSONObject.parseObject(result,Query.class);

        DynamicQueryDto dto=new DynamicQueryDto();
        dto.setTableName(formObject.getTableName());

        dto.setQueryWhere(query.parseWhere());
        dto.setQueryWhereAppend(" t1.id="+instanceId);

        Integer count=dynamicFormMapper.selectCount(dto);

        return count>0?true:false;
    }
}
