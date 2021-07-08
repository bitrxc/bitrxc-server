package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/9
 */
public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

}
