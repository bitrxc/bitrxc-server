package cn.edu.bit.ruixin.community.service;

import cn.edu.bit.ruixin.community.domain.Role;

import java.util.List;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/9
 */
public interface RoleService {
    List<Role> getRolesByAdminId(Integer id);
}
