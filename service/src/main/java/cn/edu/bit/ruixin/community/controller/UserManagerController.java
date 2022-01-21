package cn.edu.bit.ruixin.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.bit.ruixin.base.common.exp.CommonResult;
import cn.edu.bit.ruixin.community.annotation.MsgSecCheck;
import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.service.UserService;
import cn.edu.bit.ruixin.community.vo.UserInfoVo;
import lombok.Data;

/**
 * 
 *
 * @author jingkaimori
 * @date 2021/2/5
 */
@RestController
@CrossOrigin
@RequestMapping("/admin/user")
public class UserManagerController {

    @Autowired
    private UserService userService;
    
    @PreAuthorize("hasAuthority('user')")
    @MsgSecCheck("infoVo")
    @PostMapping("")
    public CommonResult<Object> modifyUser(@RequestBody(required = true)UserInfoVo infoVo) {
        User user = UserInfoVo.convertToPo(infoVo);
        userService.modifyUser(user);
        return CommonResult.ok(Object.class).msg("修改用户信息成功!");
    }

    @PreAuthorize("hasAnyAuthority('room','appointCheck')")
    @GetMapping("/byWxid")
    public CommonResult<UserInfoReturnVo> getUserInfoByWxid(
        @RequestParam("username") String username
    ){
        User user = userService.getUserByUsername(username);
        return CommonResult.ok(UserInfoReturnVo.class)
            .data(new UserInfoReturnVo(user));
    }

    @PreAuthorize("hasAnyAuthority('room','appointCheck')")
    @GetMapping("/bySchoolId")
    public CommonResult<UserInfoReturnVo> getUserInfoBySchoolId(
        @RequestParam("schoolId") String schoolId
    ){
        User user = userService.getUserBySchoolId(schoolId);
        return CommonResult.ok(UserInfoReturnVo.class)
            .data(new UserInfoReturnVo(user));
    }
}

@Data
class UserInfoReturnVo{
    private UserInfoVo userInfo;

    UserInfoReturnVo(User user){
        this.userInfo = UserInfoVo.convertToVo(user);
    }
}