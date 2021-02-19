package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.base.security.utils.DefaultPasswordEncoder;
import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.exception.UserDaoException;
import cn.edu.bit.ruixin.community.repository.UserRepository;
import cn.edu.bit.ruixin.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/5
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("defaultPasswordEncoder")
    private DefaultPasswordEncoder passwordEncoder;

    /**
     * 根据微信id查询用户
     * @param username
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void registerNewUser(User user) {
        String source = user.getPassword();

        String encode = passwordEncoder.encode(source);
        user.setPassword(encode);
        userRepository.save(user);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void modifyUser(User user) {
        User userByUsername = userRepository.findUserByUsername(user.getUsername());
        if (userByUsername != null) {
            String username = user.getUsername();
            String name = user.getName();
            String organization = user.getOrganization();
            String password = user.getPassword();
            String phone = user.getPhone();

            if (username!=null && !username.equals("")) {
                userByUsername.setUsername(username);
            }
            if (name!=null && !name.equals("")) {
                userByUsername.setName(name);
            }
            if (organization!=null && !organization.equals("")) {
                userByUsername.setOrganization(organization);
            }
            if (password!=null && !password.equals("")) {
                userByUsername.setPassword(password);
            }
            if (phone!= null && !phone.equals("")) {
                userByUsername.setPhone(phone);
            }

            userRepository.save(userByUsername);

        } else {
            throw new UserDaoException("该用户不存在!");
        }
    }
}
