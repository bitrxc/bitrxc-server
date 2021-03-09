package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/9
 */
public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

    @Query(nativeQuery = true, value = "SELECT t2.id AS id, t2.role_name AS role_name FROM `admin_role` AS t1 LEFT JOIN `role` AS t2 ON t1.role_id = t2.id\n" +
            "WHERE t1.admin_id = ?")
    List<Role> findRolesByAdminId(Integer id);
}
