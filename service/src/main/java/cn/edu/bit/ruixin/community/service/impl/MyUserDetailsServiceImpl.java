package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.exception.UserDaoException;
import cn.edu.bit.ruixin.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/5
 */
@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询数据库
        User user = userService.getUserByUsername(username);
        System.out.println(user);

        if (user != null) {
            // 查询权限表，填充权限字段

            return user;
        } else {
          throw new UserDaoException("此微信用户尚未注册!");
        }
    }
}
