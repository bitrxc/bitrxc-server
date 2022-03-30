package cn.edu.bit.secondclass.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product")
public class Product {
    /** 文创产品编号 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    /** 文创产品名称 */
    @Column(name = "name")
    private String name;
    /** 此文创产品的剩余库存 */
    @Column(name = "stock")
    private int stock;
    /** 兑换此文创产品所需要的积分 */
    @Column(name = "point")
    private int point;
}
