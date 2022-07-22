package cn.edu.bit.secondclass.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
public class User {
    /** 用户id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    /** 用户积分 */
    @Column(name = "point")
    private int point;
    /** 总学时 */
    @Column(name = "class_hour")
    private double classHour;
    /** 主题讲座学时 */
    @Column(name = "lecture_hour")
    private double lectureHour;
    /** 用户学号 */
    @Column(name = "school_id")
    private String schoolId;
}

