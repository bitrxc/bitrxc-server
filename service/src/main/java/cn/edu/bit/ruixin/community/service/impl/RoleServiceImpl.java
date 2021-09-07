package cn.edu.bit.ruixin.community.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.bit.ruixin.community.domain.Role;
import cn.edu.bit.ruixin.community.repository.RoleRepository;
import cn.edu.bit.ruixin.community.service.RoleService;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/9
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getRolesByAdminId(Integer id) {
        return roleRepository.findAdminRoles(id);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> getRoles(List<Integer> ids) {
        List<Role> roles = new ArrayList<Role>();
        for(int rid : ids){
            roles.add(roleRepository.getOne(rid));
        }
        return roles;
    }
    
}
