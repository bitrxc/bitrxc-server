package cn.edu.bit.ruixin.community.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.bit.ruixin.base.security.utils.DefaultPasswordEncoder;
import cn.edu.bit.ruixin.community.domain.Admin;
import cn.edu.bit.ruixin.community.domain.AdminRole;
import cn.edu.bit.ruixin.community.exception.UserDaoException;
import cn.edu.bit.ruixin.community.repository.AdminRepository;
import cn.edu.bit.ruixin.community.repository.AdminRoleRepository;
import cn.edu.bit.ruixin.community.service.AdminService;
import lombok.extern.apachecommons.CommonsLog;

/**
 * TODO 制定事务管理
 *
 * @author 78165
 * @date 2021/2/21
 */
@Service
@CommonsLog
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
            String password = adminByUsername.getPassword();
//            password = passwordEncoder.encode(password);
            if (!password.equals(admin.getPassword())) {
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
    
    /**
     * 将管理员的权限设置为参数列表中的权限，并移除先前所有权限。
     * 就地更新 admin_role 表的记录
     * @return rid列表对应的角色列表
     */
    @Override
    public Admin modifyAdminRoleByAdminId(int aid,List<Integer> rids) {
        log.debug(rids);
        List<AdminRole> adminRoles = adminRoleRepository.findAdminRolesByAdminId(aid);
        Iterator<AdminRole> adminRolesIt = adminRoles.iterator();
        for(int rid : rids){
            if(adminRolesIt.hasNext()){
                AdminRole curAR = adminRolesIt.next();
                curAR.setAdminId(aid);
                curAR.setRoleId(rid);
            }else{
                // 此处不能使用{@link saveall}方法来保存新增的权限，因为saveall不会自动生成ID
                adminRoleRepository.assignRoleToAdmin(aid, rid);
            }
        }
        // 删除多余的权限
        List<AdminRole> removableAdminRoles = new ArrayList<AdminRole>();
        while(adminRolesIt.hasNext()){
            removableAdminRoles.add(adminRolesIt.next()) ;
            adminRolesIt.remove();
        }
        adminRoleRepository.saveAll(adminRoles);
        adminRoleRepository.deleteAll(removableAdminRoles);
        Admin admin = adminRepository.getOne(aid) ;
        return admin;
    }
}
