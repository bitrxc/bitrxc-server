package cn.edu.bit.ruixin.community.vo;

import cn.edu.bit.ruixin.community.annotation.FieldNeedCheck;
import cn.edu.bit.ruixin.community.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/5
 */
@Data
@NoArgsConstructor
public class UserInfoVo {

    // wxid
    private String username;

    // 标记注解，需要敏感词过滤
    @FieldNeedCheck
    private String name;

    private String phone;

    private String password;

    private String schoolId;

    @FieldNeedCheck
    private String organization;

    private Boolean checked;

    /**
     * 该方法会忽略传入的用户检查标志，即{@link UserInfoVo#checked checked}字段
     */
    public static User convertToPo(UserInfoVo infoVo) {
        User user = new User();
        user.setUsername(infoVo.getUsername());
        user.setName(infoVo.getName());
        user.setPhone(infoVo.getPhone());
        user.setOrganization(infoVo.getOrganization());
        user.setPassword(infoVo.getPassword());
        user.setSchoolId(infoVo.schoolId);
        return user;
    }

    public static UserInfoVo convertToVo(User user) {
        UserInfoVo infoVo = new UserInfoVo();
        infoVo.setName(user.getName());
        infoVo.setOrganization(user.getOrganization());
        infoVo.setPhone(user.getPhone());
        infoVo.setUsername(user.getUsername());
        infoVo.setSchoolId(user.getSchoolId());
        infoVo.setChecked(user.getChecked());
        return infoVo;
    }

    @Override
    public String toString() {
        return "UserInfoVo{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", schoolId='" + schoolId + '\'' +
                ", organization='" + organization + '\'' +
                '}';
    }
}
