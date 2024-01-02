package com.ruoyi.dynamicform.dto;

import lombok.Data;

import java.util.List;

@Data
public class TaskQueryDto {
    private String userName;
    private List<Long> groupIds;

    private String queryFlow;

    public TaskQueryDto(){

    }

    public TaskQueryDto(String queryFlow,String userName,List<Long> groupIds){
        this.queryFlow=queryFlow;
        this.userName=userName;
        this.groupIds=groupIds;
    }
}
