package cn.edu.bit.ruixin.community.domain;

import lombok.Data;
import javax.persistence.*;
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
    /** 用户的微信openid */
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
}
