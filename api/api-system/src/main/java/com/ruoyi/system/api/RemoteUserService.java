package com.ruoyi.system.api;

import com.ruoyi.system.api.domain.SysUserRoleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.api.factory.RemoteUserFallbackFactory;
import com.ruoyi.system.api.model.LoginUser;

import java.util.List;

/**
 * 用户服务
 *
 * @author ruoyi
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService
{
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @param source 请求来源
     * @return 结果
     */
    @GetMapping("/user/info/{username}")
    public R<LoginUser> getUserInfo(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @GetMapping("/user/getByName/{username}")
    public R<SysUser> getUserByName(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 注册用户信息
     *
     * @param sysUser 用户信息
     * @param source 请求来源
     * @return 结果
     */
    @PostMapping("/user/register")
    public R<Boolean> registerUserInfo(@RequestBody SysUser sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    @PostMapping("/user/selectUserByRoles")
    public R<List<String>> selectUserByRoles(@RequestBody SysUserRoleDto sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PostMapping("/user/selectLeader")
    public R<String> selectLeader(@RequestBody SysUserRoleDto sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PostMapping("/role/selectRoleListByUserId")
    public R<List<Long>> selectRoleListByUserId(@RequestBody SysUserRoleDto sysUser, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    @PutMapping("/user/profile/updatePwd")
    public R<String> updatePwd(@RequestParam("oldPassword") String oldPassword,@RequestParam("newPassword") String newPassword);
}
