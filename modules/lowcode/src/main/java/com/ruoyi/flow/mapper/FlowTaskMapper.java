package com.ruoyi.flow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.flow.domain.FlowTask;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlowTaskMapper extends BaseMapper<FlowTask> {
    List<FlowTask> getTaskHistory(FlowTask dto);
}
