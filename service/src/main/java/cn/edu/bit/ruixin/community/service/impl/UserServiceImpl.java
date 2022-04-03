package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.base.security.utils.DefaultPasswordEncoder;
import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.exception.ResourceNotFoundException;
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
import org.springframework.util.StringUtils;

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
            throw new ResourceNotFoundException("用户不存在或未录入系统");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserBySchoolId(String schoolId) {
        User user = userRepository.findUserBySchoolId(schoolId);
        if(user != null){
            return user;
        }else{
            throw new ResourceNotFoundException("用户不存在或未录入系统");
        }
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void registerNewUser(User user) {
        String source = user.getPassword();

        if (StringUtils.hasText(source)) {
            String encode = passwordEncoder.encode(source);
            user.setPassword(encode);
        }
        user.setId(null);
        user.setChecked(false);

        userRepository.save(user);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void registerNewUser(String userwxid) {
        User user = new User();
        user.setUsername(userwxid);
        user.setId(null);
        user.setChecked(false);
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

    /**
     * 保存用户信息
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public void modifyUser(User user) {
        User userByUsername = userRepository.findUserByUsername(user.getUsername());
        if (userByUsername != null) {
            String name = user.getName();
            String organization = user.getOrganization();
            String password = user.getPassword();
            String phone = user.getPhone();
            String schoolId = user.getSchoolId();

            userByUsername.setName(name);
            userByUsername.setOrganization(organization);
            userByUsername.setPassword(password);
            userByUsername.setPhone(phone);
            userByUsername.setSchoolId(schoolId);
            userByUsername.setChecked(autoCheck(user));

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

    /**
     * 检查该用户是否满足预约条件。目前的逻辑为信息齐全即可预约
     * TODO: 当用户在可信用户表内才能预约
     * @param user
     * @return
     */
    private Boolean autoCheck(User user) {
        return 
            StringUtils.hasText(user.getName()) && 
            StringUtils.hasText(user.getOrganization()) && 
            StringUtils.hasText(user.getPhone()) && 
            StringUtils.hasText(user.getSchoolId());
    }
}
