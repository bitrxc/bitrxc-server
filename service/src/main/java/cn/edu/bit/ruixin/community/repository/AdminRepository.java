package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * TODO
 *
 * This repository records everything about Admin.
 * 
 * @author 78165
 * @date 2021/2/21
 */
public interface AdminRepository extends 
    JpaRepository<Admin, Integer>, JpaSpecificationExecutor<Admin> {
    Admin findAdminByUsername(String username);

}
