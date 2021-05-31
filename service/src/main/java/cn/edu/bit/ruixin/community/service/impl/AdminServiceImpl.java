package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.base.security.utils.DefaultPasswordEncoder;
import cn.edu.bit.ruixin.community.domain.Admin;
import cn.edu.bit.ruixin.community.domain.AdminRole;
import cn.edu.bit.ruixin.community.exception.UserDaoException;
import cn.edu.bit.ruixin.community.repository.AdminRepository;
import cn.edu.bit.ruixin.community.repository.AdminRoleRepository;
import cn.edu.bit.ruixin.community.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * TODO 制定事务管理
 *
 * @author 78165
 * @date 2021/2/21
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DefaultPasswordEncoder passwordEncoder;

    @Autowired
    private AdminRoleRepository adminRoleRepository;

    @Override
    public Admin registerAdmin(Admin admin) {
        adminRepository.save(admin);
        return admin;
    }

    @Override
    public Admin login(Admin admin) {
        if (admin == null || admin.getUsername() == null || "".equals(admin.getUsername()) || admin.getPassword() == null || "".equals(admin.getPassword())) {
            throw new UserDaoException("用户名密码不能为空!");
        }
        else {
            String username = admin.getUsername();
            Admin adminByUsername = adminRepository.findAdminByUsername(username);
            String password = admin.getPassword();
//            password = passwordEncoder.encode(password);
            if (!password.equals(adminByUsername.getPassword())) {
                throw new UserDaoException("用户名密码错误!");
            } else {
                return adminByUsername;
            }
        }
    }

    @Override
    public Page<Admin> getAdminPages(Pageable pageable, String nameLike) {
        if (nameLike!=null && !nameLike.equals("")) {
            Admin admin = new Admin();
            admin.setUsername(nameLike);
            Example<Admin> example = Example.of(admin);
            return adminRepository.findAll(example, pageable);
        }
        return adminRepository.findAll(pageable);
    }

    @Override
    public Admin modifyAdminById(int id, String email, String mobile) {
        Admin admin = adminRepository.getOne(id);
        if (email != null && !email.equals("")) {
            admin.setEmail(email);
        }
        if (mobile!=null && !mobile.equals("")) {
            admin.setMobile(mobile);
        }
        adminRepository.save(admin);
        return admin;
    }

    @Override
    public void deleteAdminById(int id) {
        adminRepository.deleteById(id);
    }

    @Override
    public void assignRoleToAdmin(int id, int role_id) {
        AdminRole adminRole = new AdminRole();
        adminRole.setAdminId(id);
        adminRole.setRoleId(role_id);
        adminRoleRepository.save(adminRole);
    }
}
