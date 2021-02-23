package cn.edu.bit.ruixin.community.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/21
 */
@Data
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "mobile")
    private String mobile;
    @Column(name = "email")
    private String email;
}
