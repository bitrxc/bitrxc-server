package cn.edu.bit.ruixin.community.service;

import java.util.List;

import cn.edu.bit.ruixin.community.domain.Admin;
import cn.edu.bit.ruixin.community.domain.Permission;
import cn.edu.bit.ruixin.community.domain.Role;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/5/15
 */
public interface PermissionService {
    boolean addPermission();

    Permission getPermissionByURL(String url);

    Boolean checkPermission(Permission perm, List<Role> roles);

    List<Permission> getPermissionsByRoles(List<Role> roles);

    List<Permission> getPermissionsByAdmin(Admin admin);
}
