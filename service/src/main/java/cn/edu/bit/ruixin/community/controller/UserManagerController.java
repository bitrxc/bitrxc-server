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
 * 管理员用户信息更改接口
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
    
    /**
     * 修改用户信息
     * @param infoVo 包含用户填写的各个字段的结构体
     * @return
     */
    @PreAuthorize("hasAuthority('user')")
    @MsgSecCheck("infoVo")
    @PostMapping("")
    public CommonResult<Object> modifyUser(@RequestBody(required = true)UserInfoVo infoVo) {
        User user = UserInfoVo.convertToPo(infoVo);
        userService.modifyUser(user);
        return CommonResult.ok(Object.class).msg("修改用户信息成功!");
    }

    /**
     * 修改用户的验证状态
     * @param userid 用户Id
     * @param check 用户为合法用户是，此参数填true
     * @return
     */
    @PreAuthorize("hasAuthority('user')")
    @PostMapping("/check")
    public CommonResult<Object> checkUser(
        @RequestParam(required = true)int userid,@RequestParam(required = true)boolean check
    ){
        userService.checkUser(userid, check);
        return CommonResult.ok(Object.class).msg("修改用户状态成功!");
    }

    /**
     * 通过微信id查找用户
     * @param username 微信Id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('room','appointCheck')")
    @GetMapping("/byWxid")
    public CommonResult<UserInfoReturnVo> getUserInfoByWxid(
        @RequestParam("username") String username
    ){
        User user = userService.getUserByUsername(username);
        return CommonResult.ok(UserInfoReturnVo.class)
            .data(new UserInfoReturnVo(user));
    }

    /**
     * 通过用户学号来查找用户
     * @see {@link AppointmentManagerController#lookupAppointmentBySchoolId(int, int, String) lookupAppointmentBySchoolId}
     * @param schoolId
     * @return
     */
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
    /** 返回的用户信息 */
    private UserInfoVo userInfo;

    UserInfoReturnVo(User user){
        this.userInfo = UserInfoVo.convertToVo(user);
    }
}