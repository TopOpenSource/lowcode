package com.ruoyi.dynamicform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.dynamicform.domain.InstRelation;
import com.ruoyi.dynamicform.mapper.InstRelationMapper;
import com.ruoyi.dynamicform.service.IInstRelationService;
import liquibase.pro.packaged.Q;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstRelationService extends ServiceImpl<InstRelationMapper, InstRelation> implements IInstRelationService {

    //source字段
    public static String INST_SOURCE="source_inst_id";
    //target字段
    public static String INST_TARGET="target_inst_id";

    @Override
    public void delBathByInstId(List<Long> idList, String source_target) {
        QueryWrapper<InstRelation> queryWrapper=new QueryWrapper<>();
        queryWrapper.in(source_target,idList);
        this.baseMapper.delete(queryWrapper);
    }
}
