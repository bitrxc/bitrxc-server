package cn.edu.bit.secondclass.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "admin")
public class Admin {
    /** 管理员id **/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    /** 管理员姓名 **/
    @Column(name = "name")
    private String name;
}
