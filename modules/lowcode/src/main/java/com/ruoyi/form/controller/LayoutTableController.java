package com.ruoyi.form.controller;

import com.ruoyi.common.ResultDto;
import com.ruoyi.common.constant.SystemCodeEnum;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.form.domain.LayoutTable;
import com.ruoyi.form.dto.LayoutTableDto;
import com.ruoyi.form.service.LayoutTableService;
import com.ruoyi.system.api.model.LoginUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/form/sysFormLayoutTable")
@Log4j2
public class LayoutTableController {
    @Autowired
    private LayoutTableService sysFormLayoutTableService;

    /**
     * 查询业务对象下的表格列表
     * @param dto
     * @return
     */
    @RequestMapping("selectByObjectId")
    public List<LayoutTable> selectByObjectId(@RequestBody LayoutTable dto){
        return sysFormLayoutTableService.selectByDto(dto);
    }
    @RequestMapping("selectDtoById")
    public LayoutTableDto selectDtoById(@RequestBody LayoutTable dto){
        return sysFormLayoutTableService.selectDtoByPK(dto.getId());
    }

    /**
     * 添加
     * @param dto
     * @return
     */
    @RequestMapping("insertOrUpdate")
    public ResultDto insertOrUpdate(@RequestBody LayoutTableDto dto){
        if(dto.getId() == null){
            sysFormLayoutTableService.insert(dto);
        }else{
            sysFormLayoutTableService.updateSelectiveDtoByPK(dto);
        }
        return new ResultDto(SystemCodeEnum.SYSTEM_OK);
    }

    /**
     * 启用 停用
     * @param dto
     * @return
     */
    @RequestMapping("enable")
    public ResultDto enable(@RequestBody LayoutTable dto){
        ResultDto result=new ResultDto(SystemCodeEnum.SYSTEM_OK);
        LoginUser loginUser= SecurityUtils.getLoginUser();
        dto.setModifiedAccount(loginUser.getUsername());
        sysFormLayoutTableService.enable(dto);
        return result;
    }

    /**
     * 删除布局
     * @param dto
     * @return
     */
    @RequestMapping("delete")
    public ResultDto delete(@RequestBody LayoutTable dto) {
        ResultDto result=new ResultDto(SystemCodeEnum.SYSTEM_OK);
        //租户
        LoginUser loginUser= SecurityUtils.getLoginUser();
        sysFormLayoutTableService.delete(dto.getId());
        return result;
    }

}
