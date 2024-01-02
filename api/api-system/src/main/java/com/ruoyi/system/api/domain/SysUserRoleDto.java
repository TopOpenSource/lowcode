package com.ruoyi.system.api.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class SysUserRoleDto {
    private List<String> roleIds;
    private String userName;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    public SysUserRoleDto() {

    }


    public SysUserRoleDto(List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public SysUserRoleDto(String userName) {
        this.userName = userName;
    }

    public SysUserRoleDto(Long userId) {
        this.userId = userId;
    }
}
