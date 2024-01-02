package com.ruoyi.form.controller;

import com.ruoyi.common.ResultDto;
import com.ruoyi.common.constant.SystemCodeEnum;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.common.utils.Snowflake;
import com.ruoyi.form.domain.LayoutView;
import com.ruoyi.form.service.LayoutViewService;
import com.ruoyi.system.api.model.LoginUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/form/layoutView")
@Log4j2
public class LayoutViewController {
    @Autowired
    private LayoutViewService layoutViewService;

    private static Snowflake snowflake = new Snowflake();
    /**
     * 查询业务对象下的视图列表
     * @param dto
     * @return
     */
    @RequestMapping("selectByObjectId")
    public List<LayoutView> selectByObjectId(@RequestBody LayoutView dto){
        LoginUser loginUser= SecurityUtils.getLoginUser();
        return layoutViewService.selectByDto(dto);
    }

    /**
     * 启用 停用
     * @param dto
     * @return
     */
    @RequestMapping("enable")
    public ResultDto enable(@RequestBody LayoutView dto){
        ResultDto result=new ResultDto(SystemCodeEnum.SYSTEM_OK);
        LoginUser loginUser= SecurityUtils.getLoginUser();
        dto.setModifiedAccount(loginUser.getUsername());
        layoutViewService.enable(dto);
        return result;
    }

    /**
     * 删除布局
     * @param dto
     * @return
     */
    @RequestMapping("delete")
    public ResultDto delete(@RequestBody LayoutView dto) {
        ResultDto result=new ResultDto(SystemCodeEnum.SYSTEM_OK);
        //租户
        LoginUser loginUser= SecurityUtils.getLoginUser();
        layoutViewService.delete(dto.getId());
        return result;
    }

    /**
     *
     * @param dto
     * @return
     */
    @RequestMapping("saveOrUpdate")
    public ResultDto addView(@RequestBody LayoutView dto) {
        ResultDto resultDto = new ResultDto(SystemCodeEnum.SYSTEM_OK);
        LoginUser loginUser= SecurityUtils.getLoginUser();
        dto.setCreateAccount(loginUser.getUsername());
        dto.setModifiedAccount(loginUser.getUsername());
        if(dto.getId()!=null){
            layoutViewService.updateByPK(dto);
        }else{
            dto.setId(snowflake.nextId());
            layoutViewService.insert(dto);
        }
        return resultDto;
    }

    /**
     * 查看视图详情
     * @param dto
     * @return
     */
    @RequestMapping("selectByPK")
    public LayoutView selectByPK(@RequestBody LayoutView dto){
        //租户
        LoginUser loginUser= SecurityUtils.getLoginUser();
        return layoutViewService.selectByPK(dto.getId());
    }

    /**
     * 查询列表视图
     * @param dto
     * @return
     */
    @RequestMapping("selectByObjectId4List")
    public List<LayoutView> selectByObjectId4List(@RequestBody LayoutView dto){
        //租户
        LoginUser loginUser= SecurityUtils.getLoginUser();
        return layoutViewService.selectByDto(dto);
    }
}
