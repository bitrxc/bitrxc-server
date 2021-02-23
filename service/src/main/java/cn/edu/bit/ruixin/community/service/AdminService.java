package cn.edu.bit.ruixin.community.service;

import cn.edu.bit.ruixin.community.domain.Admin;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/21
 */

public interface AdminService {
    void registerAdmin(Admin admin);

    Admin login(Admin admin);
}
