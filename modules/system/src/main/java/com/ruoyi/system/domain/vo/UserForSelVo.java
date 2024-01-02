package com.ruoyi.system.domain.vo;

import lombok.Data;

/**
 * 为选择框的用户列表
 */
@Data
public class UserForSelVo {
    private String userName;
    private Long userId;
    private String nickName;
    private Long deptId;
    private String deptName;
}
