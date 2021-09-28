package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.community.domain.Permission;
import cn.edu.bit.ruixin.community.domain.Role;
import cn.edu.bit.ruixin.community.repository.PermissionRepository;
import cn.edu.bit.ruixin.community.repository.RolePermissionRepository;
import cn.edu.bit.ruixin.community.service.PermissionService;

import java.util.ArrayList;
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
     * This method only accepts path like `/path/to/api`, not include scheme.
     */
    @Override
    public Permission getPermissionByURL(String url){
        return permissionRepository.findPermissionLikeUrl(url);
    }
    
    @Override
    public Boolean checkPermission(Permission permission, List<Role> roles){
        Integer perId=permission.getId();
        List<Integer> rolesId=new ArrayList<Integer>();
        
        for(Role i : roles){
            rolesId.add(i.getId());
        }
        return rolePermissionRepository.existsRolePermissionByPermissionIdEqualsAndRoleIdIn(perId, rolesId);
    }

    @Override
    public List<Permission> getPermissionsByRoles(List<Role> roles) {
        // TODO need confirm
        List<Integer> rolesId=new ArrayList<Integer>();
        
        for(Role i : roles){
            rolesId.add(i.getId());
        }
        List<Integer> permids=rolePermissionRepository.getPermissionIdByRoleIdIn(rolesId);
        List<Permission> perms = new ArrayList<Permission>();
        for(Integer i:permids){
            perms.add(permissionRepository.getOne(i));
        }
        return perms;
    };
}
