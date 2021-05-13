package cn.edu.bit.ruixin.community.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/5/7
 */
@Data
@Entity
@Table(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "url")
    private String url;
}
