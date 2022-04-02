package cn.edu.bit.ruixin.community.domain;

import lombok.Data;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * `User` 数据库表，该表存储用户的个人信息
 * TODO
 *
 * @author 78165
 * @author jingkaimori
 * @date 2021/2/5
 */
@Entity
@Table(name = "user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "wxid")
    private String username;
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "organize")
    private String organization;
    @Column(name = "password")
    private String password;
    @Column(name = "school_id")
    private String schoolId;

    /** 用户资质是否经过核验 */
    @Column(name = "checked")
    private Boolean checked;
    @Transient
    private List<String> permissionValueList = new ArrayList<>();

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//        for (String permission:permissionValueList
//        ) {
//            if (!StringUtils.isEmpty(permission)) {
//                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission);
//                authorities.add(authority);
//            }
//        }
//        return authorities;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
