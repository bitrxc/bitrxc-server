package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.base.security.utils.DefaultPasswordEncoder;
import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.exception.UserDaoException;
import cn.edu.bit.ruixin.community.repository.UserRepository;
import cn.edu.bit.ruixin.community.service.UserService;
import cn.edu.bit.ruixin.community.vo.PageVo;
import cn.edu.bit.ruixin.community.vo.UserInfoVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        User user = userRepository.findUserByUsername(username);
        if(user != null){
            return user;
        }else{
            throw new UserDaoException("用户不存在或未录入系统");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserBySchoolId(String schoolId) {
        User user = userRepository.findUserBySchoolId(schoolId);
        if(user != null){
            return user;
        }else{
            throw new UserDaoException("用户不存在或未录入系统");
        }
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void registerNewUser(User user) {
        String source = user.getPassword();

        if (source != null && !source.equals("")) {

            String encode = passwordEncoder.encode(source);
            user.setPassword(encode);
        }

        userRepository.save(user);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void checkUser(String username,boolean checked){
        User usergot = userRepository.findUserByUsername(username);
        if(usergot != null){
            User user = usergot;
            user.setChecked(checked);
            userRepository.save(user);
        } else {
            throw new UserDaoException("该用户不存在!");
        }
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
            String schoolId = user.getSchoolId();

            userByUsername.setUsername(username);
            userByUsername.setName(name);
            userByUsername.setOrganization(organization);
            userByUsername.setPassword(password);
            userByUsername.setPhone(phone);
            userByUsername.setSchoolId(schoolId);

            userRepository.save(userByUsername);

        } else {
            throw new UserDaoException("该用户不存在!");
        }
    }

    @Override
    public PageVo<UserInfoVo> getAllUsers(UserInfoVo userinfo,int page,int size) {
        User user = UserInfoVo.convertToPo(userinfo);
        Example<User> example = Example.of(user);
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userList = userRepository.findAll(example, pageable);
        Page<UserInfoVo> userVoList = userList.map((userlocal)-> UserInfoVo.convertToVo(userlocal));
        return PageVo.convertToVo(userVoList);
    }
}
