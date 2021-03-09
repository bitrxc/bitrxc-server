package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.community.domain.Admin;
import cn.edu.bit.ruixin.community.domain.Role;
import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.service.AdminService;
import cn.edu.bit.ruixin.community.service.RoleService;
import cn.edu.bit.ruixin.community.service.UserService;
import cn.edu.bit.ruixin.community.vo.AdminInfoVo;
import cn.edu.bit.ruixin.community.vo.UserInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private UserService userService;

    @Autowired
    private RoleService  roleService;

    /**
     * 管理员登录接口
     * @param infoVo {
     *               "username": "",
     *               "password": ""
     * }
     * @return
     */
    @RequestMapping("/login")
    public CommonResult login(@RequestBody(required = true) AdminInfoVo infoVo) {
        Admin admin = AdminInfoVo.convertToPo(infoVo);
        Admin loginAdmin = adminService.login(admin);

        AdminInfoVo adminInfoVo = AdminInfoVo.convertToVo(loginAdmin);
        // 添加用户角色信息
        List<Role> roles = roleService.getRolesByAdminId(loginAdmin.getId());
        adminInfoVo.setRoleList(roles);
        return CommonResult.ok(ResultCode.SUCCESS).msg("管理员登录成功!").data("userInfo", adminInfoVo);
    }

    /**
     * 获取管理员分页列表
     *
     * @return
     */
    @GetMapping("/users")
    public CommonResult getUserInfo(@RequestParam(value = "query", required = false) String nameLike, @RequestParam("pagenum") int current, @RequestParam("pagesize") int limit) {
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
     * 添加管理员
     * @param adminInfoVo
     * @return
     */
    @PostMapping("/users")
    public CommonResult addAdmin(@RequestBody AdminInfoVo adminInfoVo) {
        Admin admin = AdminInfoVo.convertToPo(adminInfoVo);
        Admin registerAdmin = adminService.registerAdmin(admin);
        AdminInfoVo infoVo = new AdminInfoVo();
        BeanUtils.copyProperties(registerAdmin, infoVo);
        return CommonResult.ok(ResultCode.SUCCESS).msg("添加成功！").data("adminInfo", infoVo);
    }


    @PutMapping("/users/{id}")
    public CommonResult modifyAdmin(@PathVariable(name = "id") int id,
                                    @RequestParam(name = "email", required = false) String email,
                                    @RequestParam(name = "mobile", required = false) String mobile) {
        Admin admin = adminService.modifyAdminById(id, email, mobile);
        AdminInfoVo infoVo = new AdminInfoVo();
        BeanUtils.copyProperties(admin, infoVo);
        infoVo.setRoleList(roleService.getRolesByAdminId(admin.getId()));
        return CommonResult.ok(ResultCode.SUCCESS).data("adminInfo", infoVo);
    }

    @DeleteMapping("/users/{id}")
    public CommonResult deleteAdminById(@PathVariable("id") int id) {
        adminService.deleteAdminById(id);
        return CommonResult.ok(ResultCode.SUCCESS).msg("删除成功！");
    }

    @PostMapping("/users/{id}/role")
    public CommonResult assignRoleToAdmin(@PathVariable("id") int id, @RequestParam("rid") int role_id) {
        adminService.assignRoleToAdmin(id, role_id);
        return CommonResult.ok(ResultCode.SUCCESS).msg("分配角色成功！");
    }
}
