package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.community.domain.Admin;
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
}
