package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/21
 */
public interface AdminRepository extends JpaRepository<Admin, Integer>, JpaSpecificationExecutor<Admin> {
    Admin findAdminByUsername(String username);

    @Query(nativeQuery = true, value = "INSERT INTO `admin_role`(`admin_id`, `role_id`) VALUES (?, ?)")
    void assignRoleToAdmin(int id, int role_id);
}
