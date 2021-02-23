package cn.edu.bit.ruixin.community.service;

import cn.edu.bit.ruixin.community.domain.User;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/5
 */
public interface UserService {
    User getUserByUsername(String username);

    void registerNewUser(User user);

    void modifyUser(User user);
}
