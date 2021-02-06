package cn.edu.bit.ruixin.community.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/6
 */
@Entity
@Table(name = "deal")
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "begin")
    private Integer begin;
    @Column(name = "end")
    private Integer end;
    @Column(name = "room")
    private Integer roomId;
    @Column(name = "launch_time")
    private Integer launchTime;
    @Column(name = "launcher")
    private String launcher;
    @Column(name = "status")
    private String status;
    @Column(name = "conductor")
    private String conductor;
    @Column(name = "note")
    private String note;
}
