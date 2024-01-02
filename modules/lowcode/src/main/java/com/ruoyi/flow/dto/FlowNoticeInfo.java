package com.ruoyi.flow.dto;

import com.ruoyi.system.api.domain.SysUser;
import liquibase.pro.packaged.S;
import lombok.Data;

import java.util.List;

/**
 * noticeInfo
 * @author H2Ocean
 * @date 2023/04/12 21:47
 **/
@Data
public class FlowNoticeInfo {

    /** 代办人信息 */
    private List<SysUser> assignees;

    /** 通知信息 */
    private String message;

}
