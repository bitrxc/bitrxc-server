package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * TODO
 *
 * This repo give the relationship between URL and permission.
 *
 * @author 78165
 * @author jingkaimori
 * @date 2021/7/8
 */
public interface PermissionRepository extends 
    JpaRepository<Permission, Integer>, JpaSpecificationExecutor<Permission> {
    Permission findPermissionByUrl(String url);
    
}
