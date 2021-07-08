package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.community.domain.Permission;
import cn.edu.bit.ruixin.community.domain.Role;
import cn.edu.bit.ruixin.community.repository.PermissionRepository;
import cn.edu.bit.ruixin.community.repository.RolePermissionRepository;
import cn.edu.bit.ruixin.community.service.PermissionService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/5/15
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Override
    public boolean addPermission() {
        return false;
    }

    /**
     * TODO: URL Injection and unsecure protocol(ssh://)
     */
    @Override
    public Permission getPermissionByURL(String url){
        return permissionRepository.findPermissionByUrl(url);
    }
    
    @Override
    public Boolean checkPermission(Permission permission, List<Role> roles){
        return rolePermissionRepository.existsRolePermissionByPermissionEqualsAndRoleIn(permission, roles);
    };
}
