package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.community.domain.Admin;
import cn.edu.bit.ruixin.community.domain.Permission;
import cn.edu.bit.ruixin.community.domain.Role;
import cn.edu.bit.ruixin.community.domain.RolePermission;
import cn.edu.bit.ruixin.community.repository.PermissionRepository;
import cn.edu.bit.ruixin.community.repository.RolePermissionRepository;
import cn.edu.bit.ruixin.community.repository.RoleRepository;
import cn.edu.bit.ruixin.community.service.PermissionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Autowired
    private RoleRepository roleRepository;

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
        List<RolePermission> permids=rolePermissionRepository.getByRoleIdIn(rolesId);
        Set<Permission> perms = new HashSet<Permission>() ;
        for(RolePermission i:permids){
            perms.add(permissionRepository.getOne(i.getPermissionId()));
        }
        return new ArrayList<Permission>(perms);
    }

    @Override
    public List<Permission> getPermissionsByAdmin(Admin admin) {
        List<Role> roles = roleRepository.findAdminRoles(admin.getId());
        List<Integer> rolesId=new ArrayList<Integer>();
        for(Role i : roles){
            rolesId.add(i.getId());
        }
        List<RolePermission> roleperms=rolePermissionRepository.getByRoleIdIn(rolesId);
        Map<Integer,RolePermission> perms = new HashMap<Integer,RolePermission>();
        for(RolePermission i:roleperms){
            perms.put(i.getPermissionId(), i);
        }
        List<Integer> permids = new ArrayList<Integer>(perms.keySet());
        return permissionRepository.findByIdIn(permids);

    };
}
