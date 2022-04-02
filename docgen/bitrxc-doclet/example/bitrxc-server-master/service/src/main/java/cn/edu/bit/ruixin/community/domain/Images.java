package cn.edu.bit.ruixin.community.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/25
 */
@Entity
@Data
@Table(name = "images")
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "room")
    private Integer room;
    @Column(name = "image_hash")
    private String imageHash;

}
