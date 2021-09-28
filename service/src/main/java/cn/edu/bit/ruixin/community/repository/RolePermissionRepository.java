package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.Permission;
import cn.edu.bit.ruixin.community.domain.Role;
import cn.edu.bit.ruixin.community.domain.RolePermission;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * TODO
 *
 * This repo give the relationship between permission and role.If given tuple 
 *   <permission,role> can be found in this repo, the permission is granted to
 *   the role.
 * SQL statements only accept id, not entire data field.
 * 
 * @author 78165
 * @author jingkaimori
 * @date 2021/7/8
 */
public interface RolePermissionRepository extends 
    JpaRepository<RolePermission, Integer>, 
    JpaSpecificationExecutor<RolePermission> {
    Boolean existsRolePermissionByPermissionIdEqualsAndRoleIdIn(Integer permission, List<Integer> roles);

    List<RolePermission> getByRoleIdIn(List<Integer> roles);
}
