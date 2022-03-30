package cn.edu.bit.secondclass.service.impl;

import cn.edu.bit.secondclass.domain.User;
import cn.edu.bit.secondclass.repository.UserRepository;
import cn.edu.bit.secondclass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserById(int id) {
        return userRepository.findUserById(id);
    }
}
