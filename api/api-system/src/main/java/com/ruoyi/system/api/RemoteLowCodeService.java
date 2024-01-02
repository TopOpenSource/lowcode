package com.ruoyi.system.api;

import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.system.api.domain.SysDictData;
import com.ruoyi.system.api.dto.CommonDto;
import com.ruoyi.system.api.dto.LayoutForm;
import com.ruoyi.system.api.dto.ObjectDTO;
import com.ruoyi.system.api.factory.RemoteLogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(contextId = "remoteLowCodeService", value = ServiceNameConstants.LOW_CODE_SERVICE, fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteLowCodeService {

    @GetMapping("/form/sysFormObject/listAll")
    public TableDataInfo listAll(ObjectDTO dto);

    @RequestMapping("/form/sysFormLayoutForm/getEnableForm")
    public LayoutForm getEnableForm(@RequestBody LayoutForm dto);

    @PostMapping(value = "/audit/submit")
    public AjaxResult submit(@RequestBody CommonDto dto);

    @GetMapping("/dict/data/getValue")
    public AjaxResult getValue(SysDictData sysDictData);


}
