package cn.edu.bit.ruixin.community.vo;

import cn.edu.bit.ruixin.community.annotation.FieldNeedCheck;
import cn.edu.bit.ruixin.community.domain.Admin;
import cn.edu.bit.ruixin.community.domain.Role;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/26
 */
@Data
public class AdminInfoVo {

    private Integer id;
    @FieldNeedCheck
    private String username;
    @FieldNeedCheck
    private String email;
    private String mobile;
    private String password;

    private List<Role> roleList = new ArrayList<>();
    private List<String> urlList = new ArrayList<>();

    public static Admin convertToPo(AdminInfoVo infoVo) {
        Admin admin = new Admin();
        BeanUtils.copyProperties(infoVo, admin);
        return admin;
    }

    public static AdminInfoVo convertToVo(Admin admin) {
        AdminInfoVo infoVo = new AdminInfoVo();
        BeanUtils.copyProperties(admin, infoVo);
        return infoVo;
    }
}
