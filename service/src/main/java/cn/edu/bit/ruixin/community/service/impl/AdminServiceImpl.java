package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.community.domain.Admin;
import cn.edu.bit.ruixin.community.exception.UserDaoException;
import cn.edu.bit.ruixin.community.repository.AdminRepository;
import cn.edu.bit.ruixin.community.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/21
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public void registerAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    @Override
    public Admin login(Admin admin) {
        if (admin == null || admin.getUsername() == null || "".equals(admin.getUsername()) || admin.getPassword() == null || "".equals(admin.getPassword())) {
            throw new UserDaoException("用户名密码不能为空!");
        }
        else {
            String username = admin.getUsername();
            Admin adminByUsername = adminRepository.findAdminByUsername(username);
            if (!adminByUsername.getPassword().equals(admin.getPassword())) {
                throw new UserDaoException("用户名密码错误!");
            } else {
                return adminByUsername;
            }
        }
    }
}
