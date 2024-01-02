package com.ruoyi.instance.service;


import com.ruoyi.common.dto.CommonDto;
import com.ruoyi.dynamicform.dto.DynamicQueryDto;
import com.ruoyi.dynamicform.dto.LayoutViewDto;
import com.ruoyi.dynamicform.dto.ViewDto;
import com.ruoyi.instance.dto.InstanceDelDto;
import com.ruoyi.instance.dto.RelQueryDto;

import java.util.List;

public interface InstanceService {
    void save(CommonDto dto);

    void update(CommonDto dto);

    CommonDto selectByPk(CommonDto dto);

    ViewDto listRefView(Long objectId);

    /**
     * 检查属性值是否重复
     * @param id
     * @param value
     * @param alias
     * @return
     */
    Integer checkRepeat(Long id,Object value,String alias);

    /**
     * 获取属性值
     * @param instanceId
     * @param attrIds
     * @return
     */
    CommonDto getAttrVal(Long objectId,Long instanceId, List<Long> attrIds);

    /**
     * 列表页查询
     * @param dto
     * @return
     */
    DynamicQueryDto genDynamicQuery(LayoutViewDto dto);

    /**
     * 查询关联的实例
     * @param dto
     * @return
     */
    List<CommonDto>  listRelInstance(RelQueryDto dto);

    /**
     * 批量删除实例
     * @param dto
     */
    void delBatch(InstanceDelDto dto);
}
