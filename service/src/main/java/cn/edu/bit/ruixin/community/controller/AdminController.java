package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.base.security.utils.TokenManager;
import cn.edu.bit.ruixin.community.annotation.MsgSecCheck;
import cn.edu.bit.ruixin.community.domain.Admin;
import cn.edu.bit.ruixin.community.domain.Permission;
import cn.edu.bit.ruixin.community.domain.Role;
import cn.edu.bit.ruixin.community.service.AdminService;
import cn.edu.bit.ruixin.community.service.PermissionService;
import cn.edu.bit.ruixin.community.service.RedisService;
import cn.edu.bit.ruixin.community.service.RoleService;
import cn.edu.bit.ruixin.community.vo.AdminInfoVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/23
 */
@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private RoleService  roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RedisService redisService;


    /**
     * 管理员登录接口
     * @param infoVo {
     *               "username": "",
     *               "password": ""
     * }
     * @return
     */
    @PostMapping("/login")
    public CommonResult login(@RequestBody(required = true) AdminInfoVo infoVo) {
        Admin admin = AdminInfoVo.convertToPo(infoVo);
        Admin loginAdmin = adminService.login(admin);

        AdminInfoVo adminInfoVo = AdminInfoVo.convertToVo(loginAdmin);
        // 添加用户角色、查询并添加用户可访问接口
        // add user's permission, gather url entry points avaliable for user, return both of them
        List<Role> roles = roleService.getRolesByAdminId(loginAdmin.getId());
        List<Permission> permissions = permissionService.getPermissionsByRoles(roles);
        List<String> urls = new ArrayList<String>();
        for(Permission i:permissions){
            urls.add(i.getUrl());
        }
        adminInfoVo.setUrlList(urls);
        adminInfoVo.setRoleList(roles);
        // 暂时补救措施：将密码设为空串以避免从redis取出内容时报错
        // TODO: adminInfoVo 移除密码字段
        adminInfoVo.setPassword("");

        // 生成token，放置Redis中
        String token = tokenManager.createTokenForAdmin(adminInfoVo.getMobile());
        try {
            redisService.opsForValueSetWithExpire(token, adminInfoVo, 30, TimeUnit.MINUTES);

            return CommonResult.ok(ResultCode.SUCCESS).msg("管理员登录成功!").data("userInfo", adminInfoVo).data("token", token);
        } catch (JsonProcessingException e) {
            return CommonResult.error(ResultCode.INTERNAL_SERVER_ERROR).msg("登录失败，请重试！");
        }
    }

    @GetMapping("/managers/roles")
    public CommonResult getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return CommonResult.ok(ResultCode.SUCCESS).data("roles",roles);
    }

    /**
     * 获取管理员分页列表
     *
     * @return
     */
    @GetMapping("/managers")
    public CommonResult getAdminPage(@RequestParam(value = "query", required = false) String nameLike, @RequestParam("pagenum") int current, @RequestParam("pagesize") int limit) {
        Pageable pageable = PageRequest.of(current, limit);
        if (nameLike != null && !nameLike.equals("")) {
            nameLike = "%" + nameLike + "%";
        }

        Page<Admin> page = adminService.getAdminPages(pageable, nameLike);

        List<Admin> list = page.getContent();
        List<AdminInfoVo> infoVos = new ArrayList<>(list.size());
        for (Admin admin :
                list) {
            AdminInfoVo infoVo = new AdminInfoVo();
            BeanUtils.copyProperties(admin, infoVo);

            List<Role> roles = roleService.getRolesByAdminId(admin.getId());

            infoVo.setRoleList(roles);

            infoVos.add(infoVo);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("totalElements", page.getTotalElements());
        map.put("totalPages", page.getTotalPages());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        map.put("items", infoVos);

        return CommonResult.ok(ResultCode.SUCCESS).data(map);
    }

    /**
     * 根据ID获取管理员信息
     *
     * @return
     */
    @GetMapping("/managers/{aid}")
    public CommonResult getAdminInfo(@PathVariable(value = "aid") int adminid) {

        Admin admin = adminService.getAdminById(adminid);
        AdminInfoVo adminInfoVo = AdminInfoVo.convertToVo(admin);
        return CommonResult.ok(ResultCode.SUCCESS).data("info",adminInfoVo);
    }
    /**
     * 添加管理员
     * @param adminInfoVo
     * @return
     */
    @MsgSecCheck("adminInfoVo")
    @PostMapping("/managers")
    public CommonResult addAdmin(@RequestBody AdminInfoVo adminInfoVo) {
        Admin admin = AdminInfoVo.convertToPo(adminInfoVo);
        Admin registerAdmin = adminService.registerAdmin(admin);
        AdminInfoVo infoVo = new AdminInfoVo();
        BeanUtils.copyProperties(registerAdmin, infoVo);
        return CommonResult.ok(ResultCode.SUCCESS).msg("添加成功！").data("adminInfo", infoVo);
    }

    @MsgSecCheck({"email"})
    @PutMapping("/managers/{id}")
    public CommonResult modifyAdmin(@PathVariable(name = "id") int id,
                                    @RequestParam(name = "email", required = false) String email,
                                    @RequestParam(name = "mobile", required = false) String mobile) {
        Admin admin = adminService.modifyAdminById(id, email, mobile);
        AdminInfoVo infoVo = new AdminInfoVo();
        BeanUtils.copyProperties(admin, infoVo);
        infoVo.setRoleList(roleService.getRolesByAdminId(admin.getId()));
        return CommonResult.ok(ResultCode.SUCCESS).data("adminInfo", infoVo);
    }

    @DeleteMapping("/managers/{id}")
    public CommonResult deleteAdminById(@PathVariable("id") int id) {
        adminService.deleteAdminById(id);
        return CommonResult.ok(ResultCode.SUCCESS).msg("删除成功！");
    }

    @PostMapping("/managers/{id}/role")
    public CommonResult assignRoleToAdmin(@PathVariable("id") int id, @RequestParam("rid") int role_id) {
        adminService.assignRoleToAdmin(id, role_id);
        return CommonResult.ok(ResultCode.SUCCESS).msg("分配角色成功！");
    }
    
    /**
     * 修改管理员权限，并更新redis缓存使权限修改生效
     * 注意，修改权限后，会产生新的 token ，新 token 内有提升的权限
     * TODO 旧 token 失效
     */
    @PostMapping("/managers/{id}/roles")
    public CommonResult modifyAdminRoles(@PathVariable(name = "id") int id,
                                    @RequestBody(required = true) List<Integer> roles) {
        Admin admin = adminService.modifyAdminRoleByAdminId(id, roles);
        List<Role> rolelist = roleService.getRoles(roles);
        AdminInfoVo infoVo = AdminInfoVo.convertToVo(admin);
        infoVo.setRoleList(rolelist);
            // 生成token，放置Redis中
        String token = tokenManager.createTokenForAdmin(infoVo.getMobile());
        try {
            redisService.opsForValueSetWithExpire(token, infoVo, 30, TimeUnit.MINUTES);

            return CommonResult.ok(ResultCode.SUCCESS).msg("权限修改成功!").data("userInfo", infoVo).data("token", token);
        } catch (JsonProcessingException e) {
            return CommonResult.error(ResultCode.INTERNAL_SERVER_ERROR).msg("权限修改失败，请重试！");
        }
        
    }
}
