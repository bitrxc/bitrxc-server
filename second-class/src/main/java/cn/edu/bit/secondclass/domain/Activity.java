package cn.edu.bit.secondclass.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "number")
    private String number;
    @Column(name = "primary_category")
    private String primaryCategory;
    @Column(name = "second_category")
    private String secondCategory;
    @Column(name = "info")
    private String info;
}
