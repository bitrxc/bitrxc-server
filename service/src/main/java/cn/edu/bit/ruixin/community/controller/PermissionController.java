package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.community.domain.Permission;
import cn.edu.bit.ruixin.community.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/5/15
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @PostMapping("")
    public CommonResult addPermission(@RequestBody(required = true)Permission permission) {
        if (permission.getUrl()!=null && !"".equals(permission.getUrl())) {
            boolean flag = permissionService.addPermission();
        }
        return null;
    }
}
