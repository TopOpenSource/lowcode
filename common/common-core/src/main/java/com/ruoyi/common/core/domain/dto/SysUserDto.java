package com.ruoyi.common.core.domain.dto;

import java.io.Serializable;

public class SysUserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // 类型为删除时，使用此集合
    private Long[] userIds;

    private Long userId;

    private String phoneNumber;

    private String password;

    private String nickName;

    // 0、删除，1新增
    private Integer type;

    public SysUserDto() {
    }

    public Long[] getUserIds() {
        return userIds;
    }

    public void setUserIds(Long[] userIds) {
        this.userIds = userIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public SysUserDto(Long userId, String phoneNumber, String password, String nickName, Integer type) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.nickName = nickName;
        this.type = type;
    }

    public SysUserDto(Long[] userIds, Integer type) {
        this.userIds = userIds;
        this.type = type;
    }
}
