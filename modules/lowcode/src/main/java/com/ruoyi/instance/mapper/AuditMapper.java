package com.ruoyi.instance.mapper;

import com.ruoyi.flow.dto.FlowTaskDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditMapper {
    List<FlowTaskDto> getMyTask(@Param("dto")FlowTaskDto flowTaskDto, @Param("userName") String userName, @Param("groupIds")  List<Long> groupIds);

    List<FlowTaskDto> getMyFinishedTask(@Param("dto")FlowTaskDto flowTaskDto, @Param("userName") String userName);

    List<FlowTaskDto> getUnSubmit(@Param("dto")FlowTaskDto flowTaskDto, @Param("userName") String userName);

    List<FlowTaskDto> getMyApplyTask(@Param("dto")FlowTaskDto flowTaskDto, @Param("userName") String userName);

    FlowTaskDto selectMyTask(@Param("userName") String userName, @Param("groupIds")  List<Long> groupIds,@Param("instId")Long instId);

}
