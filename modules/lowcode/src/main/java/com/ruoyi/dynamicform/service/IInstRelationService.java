package com.ruoyi.dynamicform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.dynamicform.domain.InstRelation;
import com.ruoyi.dynamicform.mapper.InstRelationMapper;

import java.util.List;

public interface IInstRelationService extends IService<InstRelation> {
    void delBathByInstId(List<Long> idList,String source_target);
}
