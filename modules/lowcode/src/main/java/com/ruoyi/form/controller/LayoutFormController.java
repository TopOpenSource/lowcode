package com.ruoyi.form.controller;

import com.ruoyi.common.ResultDto;
import com.ruoyi.common.constant.SystemCodeEnum;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.utils.Snowflake;
import com.ruoyi.form.domain.LayoutForm;
import com.ruoyi.form.service.LayoutFormService;
import com.ruoyi.system.api.model.LoginUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/form/sysFormLayoutForm")
@Log4j2
public class LayoutFormController {
    @Autowired
    private LayoutFormService sysFormLayoutFormService;
    private static Snowflake snowflake = new Snowflake();
    /**
     * 查询业务对象下的表单列表
     *
     * @param dto
     * @return
     */
    @RequestMapping("selectByObjectId")
    public List<LayoutForm> selectByObjectId(@RequestBody LayoutForm dto) {
        LoginUser loginUser= SecurityUtils.getLoginUser();
        return sysFormLayoutFormService.selectByDto(dto);
    }

    /**
     * 根据ID查询
     * @param dto
     * @return
     */
    @RequestMapping("selectById")
    public LayoutForm selectById(@RequestBody LayoutForm dto) {
        //租户
       LoginUser loginUser= SecurityUtils.getLoginUser();
        return sysFormLayoutFormService.selectById(dto.getId());
    }

    /**
     *  保存 修改 布局
     * @param dto
     * @return
     */
    @RequestMapping("saveOrUpdate")
    public ResultDto saveOrUpdate(@RequestBody LayoutForm dto){
        ResultDto result=new ResultDto(SystemCodeEnum.SYSTEM_OK);
        LoginUser loginUser= SecurityUtils.getLoginUser();
        dto.setModifiedAccount(loginUser.getUsername());

        if(dto.getId()==null){
            dto.setId(snowflake.nextId());
            dto.setCreateAccount(loginUser.getUsername());
            sysFormLayoutFormService.save(dto);
        }else{
            sysFormLayoutFormService.update(dto);
        }
        return result;
    }

    /**
     * 启用 停用
     * @param dto
     * @return
     */
    @RequestMapping("enable")
    public ResultDto enable(@RequestBody LayoutForm dto){
        ResultDto result=new ResultDto(SystemCodeEnum.SYSTEM_OK);
       LoginUser loginUser= SecurityUtils.getLoginUser();
        dto.setModifiedAccount(loginUser.getUsername());
        sysFormLayoutFormService.enable(dto);
        return result;
    }

    /**
     * 删除布局
     * @param dto
     * @return
     */
    @RequestMapping("delete")
    public ResultDto delete(@RequestBody LayoutForm dto) {
        ResultDto result=new ResultDto(SystemCodeEnum.SYSTEM_OK);
        //租户
       LoginUser loginUser= SecurityUtils.getLoginUser();
        sysFormLayoutFormService.delete(dto.getId());
        return result;
    }

    /**
     * 获取启用的 表单布局
     * @param dto
     * @return
     */
    @RequestMapping("getEnableForm")
    public LayoutForm getEnableForm(@RequestBody LayoutForm dto) {
        //租户
       LoginUser loginUser= SecurityUtils.getLoginUser();
        LayoutForm enableForm = sysFormLayoutFormService.getEnableForm(dto.getObjectId(), dto.getType());
        return enableForm;
    }

    @RequestMapping("hasEnableForm")
    public boolean hasEnableForm(@RequestBody LayoutForm dto) {
        return sysFormLayoutFormService.hasEnableForm(dto.getObjectId(), dto.getType());
    }

}
