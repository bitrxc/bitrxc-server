package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.community.repository.PermissionRepository;
import cn.edu.bit.ruixin.community.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/5/15
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;


}
