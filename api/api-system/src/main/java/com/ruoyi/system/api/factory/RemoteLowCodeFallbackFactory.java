package com.ruoyi.system.api.factory;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.system.api.RemoteLowCodeService;
import com.ruoyi.system.api.domain.SysDictData;
import com.ruoyi.system.api.dto.CommonDto;
import com.ruoyi.system.api.dto.LayoutForm;
import com.ruoyi.system.api.dto.ObjectDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 日志服务降级处理
 *
 * @author ruoyi
 */
@Component
public class RemoteLowCodeFallbackFactory implements FallbackFactory<RemoteLowCodeService>
{
    private static final Logger log = LoggerFactory.getLogger(RemoteLowCodeFallbackFactory.class);

    @Override
    public RemoteLowCodeService create(Throwable throwable)
    {
        log.error("低代码服务调用失败:{}", throwable.getMessage());
        return new RemoteLowCodeService()
        {
            @Override
            public TableDataInfo listAll(ObjectDTO dto) {
                return null;
            }

            @Override
            public LayoutForm getEnableForm(LayoutForm dto) {
                return null;
            }

            @Override
            public AjaxResult submit(CommonDto dto) {
                return null;
            }

            @Override
            public AjaxResult getValue(SysDictData sysDictData) {
                return null;
            }
        };

    }
}
