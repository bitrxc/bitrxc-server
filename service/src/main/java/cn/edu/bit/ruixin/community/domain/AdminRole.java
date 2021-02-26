package cn.edu.bit.ruixin.community.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/10
 */
@Data
@Table(name = "admin_role")
@Entity
public class AdminRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "admin_id")
    private int adminId;
    @Column(name = "role_id")
    private int roleId;
}
