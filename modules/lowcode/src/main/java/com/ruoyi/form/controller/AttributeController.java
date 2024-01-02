package com.ruoyi.form.controller;

import com.ruoyi.common.ResultDto;
import com.ruoyi.common.constant.SystemCodeEnum;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.utils.Snowflake;
import com.ruoyi.form.domain.AttributeType;
import com.ruoyi.form.domain.FormObjectAttribute;
import com.ruoyi.form.dto.ObjectAttributeDTO;
import com.ruoyi.form.service.ObjectAttributeService;
import com.ruoyi.system.api.model.LoginUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/form/sysFormObjectAttribute")
@Log4j2
public class AttributeController {
    @Autowired
    private ObjectAttributeService sysFormObjectAttributeService;
    private static Snowflake snowflake = new Snowflake();
    /**
     * 新增属性
     * @param dto
     * @return
     * @throws IOException
     */
    @RequestMapping("insert")
    public ResultDto insert(@RequestBody FormObjectAttribute dto) {
        //创建人
        LoginUser loginUser = SecurityUtils.getLoginUser();
        dto.setCreateAccount(loginUser.getUsername());
        //id
        dto.setId(snowflake.nextId());
        sysFormObjectAttributeService.insert(dto);
        return new ResultDto(SystemCodeEnum.SYSTEM_OK);
    }

    /**
     * 属性查询
     * @param dto
     * @return
     */
    @RequestMapping("selectByDto")
    public List<FormObjectAttribute> selectByDto(@RequestBody FormObjectAttribute dto){
        //租户
       LoginUser loginUser= SecurityUtils.getLoginUser();
        return sysFormObjectAttributeService.selectByDto(dto);
    }

    /**
     * 属性修改名称
     * @param dto
     * @return
     */
    @RequestMapping("updateName")
    public ResultDto updateName(FormObjectAttribute dto) {
        ResultDto resultDto=new ResultDto(SystemCodeEnum.SYSTEM_OK);

       LoginUser loginUser= SecurityUtils.getLoginUser();
        sysFormObjectAttributeService.updateName(dto);
        return resultDto;
    }

    /**
     * 获取字段列表
     * @param dto
     * @return
     */
    @RequestMapping("listFormObjectAttribute")
    public List<ObjectAttributeDTO> listFormObjectAttribute(@RequestBody ObjectAttributeDTO dto) {
       LoginUser loginUser= SecurityUtils.getLoginUser();
        return sysFormObjectAttributeService.listFormObjectAttribute(dto);
    }

    /**
     * 删除自定义字段操作
     * @param dto
     * @return
     */
    @RequestMapping("deleteFormObjectAttribute")
    public ResultDto deleteFormObjectAttribute(@RequestBody ObjectAttributeDTO dto) {
        ResultDto resultDto=new ResultDto(SystemCodeEnum.SYSTEM_OK);
       LoginUser loginUser= SecurityUtils.getLoginUser();
        sysFormObjectAttributeService.deleteFormObjectAttribute(dto);
        return resultDto;
    }

    /**
     * 启用/禁用字段
     * @param dto
     * @return
     */
    @RequestMapping("toggleAttributeEnable")
    public ResultDto toggleAttributeEnable(@RequestBody ObjectAttributeDTO dto) {
        ResultDto resultDto=new ResultDto(SystemCodeEnum.SYSTEM_OK);
       LoginUser loginUser= SecurityUtils.getLoginUser();
        sysFormObjectAttributeService.toggleAttributeEnable(dto);
        return resultDto;
    }


    /**
     * 获取字段类型基础模型数据
     * @return
     */
    @RequestMapping("listAttributeTypes")
    public List<AttributeType> listAttributeTypes() {

        return sysFormObjectAttributeService.listAttributeTypes();
    }

    /**
     * 新增字段
     * @param dto
     * @return
     */
    @RequestMapping("saveFormObjectAttribute")
    public ResultDto saveFormObjectAttribute(@RequestBody ObjectAttributeDTO dto) {
        //创建人
       LoginUser loginUser= SecurityUtils.getLoginUser();

        //dto.setCreateAccount(loginUser.getUsername());
        //id
        dto.setId(snowflake.nextId());
        sysFormObjectAttributeService.saveFormObjectAttribute(dto);
        return new ResultDto(SystemCodeEnum.SYSTEM_OK);
    }

    @RequestMapping("saveFormObjectAttributeMX")
    public ResultDto saveFormObjectAttributeMX(@RequestBody List<ObjectAttributeDTO> list) {
        LoginUser loginUser= SecurityUtils.getLoginUser();
        sysFormObjectAttributeService.saveFormObjectAttributeMX(list);
        return new ResultDto(SystemCodeEnum.SYSTEM_OK);
    }

    /**
     * 编辑字段
     * @param dto
     * @return
     */
    @RequestMapping("updateFormObjectAttribute")
    public ResultDto updateFormObjectAttribute(@RequestBody ObjectAttributeDTO dto) {
        //创建人
       LoginUser loginUser= SecurityUtils.getLoginUser();
        //dto.setModifiedAccount(loginUser.getUserAccount());

        sysFormObjectAttributeService.updateFormObjectAttribute(dto);
        return new ResultDto(SystemCodeEnum.SYSTEM_OK);
    }

    /**
     * 获取字段信息
     * @param dto
     * @return
     */
    @RequestMapping("getFormObjectAttribute")
    public FormObjectAttribute getFormObjectAttribute(@RequestBody FormObjectAttribute dto) {
       LoginUser loginUser= SecurityUtils.getLoginUser();

        return sysFormObjectAttributeService.selectByPK(dto.getId());
    }

    @RequestMapping("deleteAttr")
    public ResultDto deleteAttr(@RequestBody ObjectAttributeDTO dto){
        sysFormObjectAttributeService.deleteAttr(dto);
        return new ResultDto(SystemCodeEnum.SYSTEM_OK);
    }

}
