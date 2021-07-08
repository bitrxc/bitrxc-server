package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.AdminRole;
import cn.edu.bit.ruixin.community.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
/**
 * TODO
 *
 * This repository handles which groups are assigned to an admin. Notice that 
 *   in fact this repository is an array field of admin, not a independent 
 *   record.
 * 
 * @author 78165
 * @author jingkaimori
 * @date 2021/7/8
 */
public interface AdminRoleRepository extends JpaRepository<AdminRole, Integer>, JpaSpecificationExecutor<AdminRole> {

    @Query(nativeQuery = true, value = "INSERT INTO `admin_role`(`admin_id`, `role_id`) VALUES (?, ?)")
    void assignRoleToAdmin(int id, int role_id);
    
    @Query(nativeQuery = true, value = "SELECT t2.id AS id, t2.role_name AS role_name FROM `admin_role` AS t1 LEFT JOIN `role` AS t2 ON t1.role_id = t2.id\n" +
            "WHERE t1.admin_id = ?")
    List<Role> findRolesByAdminId(Integer id);
}
