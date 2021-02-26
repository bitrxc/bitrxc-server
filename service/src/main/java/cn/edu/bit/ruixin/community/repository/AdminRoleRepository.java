package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/10
 */
public interface AdminRoleRepository extends JpaRepository<AdminRole, Integer>, JpaSpecificationExecutor<AdminRole> {
}
