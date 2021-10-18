package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.Admin;
import cn.edu.bit.ruixin.community.domain.AdminRole;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * TODO
 *
 * This repository handles which groups are assigned to an admin. Notice that 
 *   in fact this repository is an array field of admin, not a independent 
 *   record.
 * 
 * @author 78165
 * @author jingkaimori
 * @date 2021/7/18
 */
public interface AdminRoleRepository extends JpaRepository<AdminRole, Integer>, JpaSpecificationExecutor<AdminRole> {

    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO `admin_role`(`admin_id`, `role_id`) VALUES (?, ?)")
    void assignRoleToAdmin(int id, int role_id);

    List<AdminRole> findAdminRolesByAdminId(int aid);
}
