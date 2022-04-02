package cn.edu.bit.ruixin.community.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/10
 */
@Entity
@Table(name = "schedule")
@Data
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "begin")
    private String begin;

    @Column(name = "end")
    private String end;
}
