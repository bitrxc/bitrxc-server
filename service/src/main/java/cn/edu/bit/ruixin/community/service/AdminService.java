package cn.edu.bit.ruixin.community.service;

import cn.edu.bit.ruixin.community.domain.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/21
 */

public interface AdminService {
    Admin registerAdmin(Admin admin);

    Admin login(Admin admin);

    Page<Admin> getAdminPages(Pageable pageable, String nameLike);

    Admin modifyAdminById(int id, String email, String mobile);

    void deleteAdminById(int id);

    void assignRoleToAdmin(int id, int role_id);
}
