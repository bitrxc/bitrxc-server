package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/5/7
 */
public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer>, JpaSpecificationExecutor<RolePermission> {
}
