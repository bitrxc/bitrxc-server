package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.community.domain.Role;
import cn.edu.bit.ruixin.community.repository.AdminRoleRepository;
import cn.edu.bit.ruixin.community.repository.RoleRepository;
import cn.edu.bit.ruixin.community.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/9
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private AdminRoleRepository adminRoleRepository;

    @Override
    public List<Role> getRolesByAdminId(Integer id) {
        return adminRoleRepository.findRolesByAdminId(id);
    }
}
