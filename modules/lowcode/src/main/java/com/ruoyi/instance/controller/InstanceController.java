package com.ruoyi.instance.controller;

import com.ruoyi.common.ResultDto;
import com.ruoyi.common.constant.SystemCodeEnum;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.dto.CommonDto;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.utils.Snowflake;
import com.ruoyi.dynamicform.dto.*;
import com.ruoyi.dynamicform.service.DynamicFormService;
import com.ruoyi.instance.dto.InstanceDelDto;
import com.ruoyi.instance.dto.RelQueryDto;
import com.ruoyi.instance.service.InstanceService;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysUserRoleDto;
import com.ruoyi.system.api.model.LoginUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/lowcode/instance")
@Log4j2
public class InstanceController extends BaseController {
    private static Snowflake snowflake = new Snowflake();
    @Autowired
    private InstanceService instanceService;

    @Autowired
    private DynamicFormService dynamicFormService;

    @Autowired
    private RemoteUserService remoteUserService;
    /**
     *  保存 修改 客户基本信息
     * @param dto
     * @return
     */
    @RequestMapping("saveOrUpdate")
    public ResultDto saveOrUpdate(@RequestBody CommonDto dto){
        ResultDto result=new ResultDto(SystemCodeEnum.SYSTEM_OK);
       // LoginUserInfo loginUser=LoginUserInfo.getLoginUserInfo();
        LoginUser loginUser= SecurityUtils.getLoginUser();
        if(dto.get("id")!=null){
            dto.put("modifiedAccount",loginUser.getUsername());
            instanceService.update(dto);
        }else{
            dto.put("id",snowflake.nextId());
            dto.put("modifiedAccount",loginUser.getUsername());
            dto.put("createAccount",loginUser.getUsername());
            instanceService.save(dto);
        }
        return result;
    }

    /**
     * 详情查看
     * @param dto
     * @return
     */
    @RequestMapping("selectByPk")
    public CommonDto selectByPk(@RequestBody CommonDto dto) {
        return instanceService.selectByPk(dto);
    }

    /**
     * 检查属性值是否重复
     * @param dto
     * @return
     */
    @RequestMapping("checkRepeat")
    public Integer checkRepeat(@RequestBody CommonDto dto) {
        return instanceService.checkRepeat(dto.getAsLong("id"),dto.get("value"),dto.getAsString("alias"));
    }


    /**
     * 列表查询
     * @param dto
     * @return
     */
    @PostMapping("/pageList")
    public TableDataInfo pageList(@RequestBody LayoutViewDto dto) {
        DynamicQueryDto queryDto = instanceService.genDynamicQuery(dto);
        startPage();

        List<CommonDto> list;

        if(StringUtils.isEmpty(dto.getQueryFlow())){
           list=dynamicFormService.selectList(queryDto);
        }else{
            LoginUser loginUser= SecurityUtils.getLoginUser();
            R<List<Long>> roleIds = remoteUserService.selectRoleListByUserId(new SysUserRoleDto(loginUser.getUserid()), SecurityConstants.INNER);
            TaskQueryDto taskQueryDto=new TaskQueryDto(dto.getQueryFlow(),loginUser.getUsername(),roleIds.getData());
            list=dynamicFormService.selectListWithTask(queryDto,taskQueryDto);
        }

        return getDataTable(list);
    }

    /**
     * 查询关联视图
     * @param objectId
     * @return
     */
    @GetMapping("/selRefView/{objectId}")
    public ViewDto selRefView(@PathVariable("objectId") Long objectId) {
        ViewDto list=instanceService.listRefView(objectId);
        return list;
    }

    /**
//     * 查询关联的实例ID
     * @return
     */
    @PostMapping("/listRelInstance")
    public List<CommonDto>  listRelInstance(@RequestBody  RelQueryDto dto){
        return instanceService.listRelInstance(dto);
    }

    /**
     * 查询表格布局
     * @param objectId
     * @return
     */
    @GetMapping("/selTable/{objectId}")
    public TableDto selTable(@PathVariable("objectId") Long objectId){
        LoginUser loginUser= SecurityUtils.getLoginUser();
        Set<String> roles = loginUser.getRoles();
        return dynamicFormService.selectTableDto(objectId,roles);
    }


    /**
     * 实例删除
     * @param dto
     * @return
     */
    @PostMapping("delBatch")
    public AjaxResult delBatch(@RequestBody InstanceDelDto dto){
        instanceService.delBatch(dto);
        return AjaxResult.success();
    }

}
