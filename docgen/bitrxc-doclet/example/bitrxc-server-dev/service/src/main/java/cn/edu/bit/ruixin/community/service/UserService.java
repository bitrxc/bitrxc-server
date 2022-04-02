package cn.edu.bit.ruixin.community.service;

import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.vo.PageVo;
import cn.edu.bit.ruixin.community.vo.UserInfoVo;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/5
 */
public interface UserService {
    PageVo<UserInfoVo> getAllUsers(UserInfoVo userinfo,int page,int size);

    User getUserBySchoolId(String schoolId);

    User getUserByUsername(String username);

    void registerNewUser(User user);

    void modifyUser(User user);

    void checkUser(String username,boolean checked);
}
