package cn.edu.bit.ruixin.community.domain;

import lombok.Data;

import java.util.Date;

import javax.persistence.*;

/**
 * 会议信息
 */
@Entity
@Table(name = "meeting")
@Data
public class Meeting {
    /** 活动ID，与房间预约ID相同 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /** 相关的房间预约信息 */
    @Column(name = "appointment_id")
    @OneToOne
    @JoinColumn(name = "appointment_id",referencedColumnName = "id")
    private Appointment appointment;
    /** 活动名称 */
    @Column(name = "name")
    private String name;
    /** 活动介绍 */
    @Column(name = "description")
    private String description;
    /** 发起此活动的用户 */
    @Column(name = "launcher_id")
    private User launcher;
    /** 活动开始时间 */
    @Column(name = "begin")
    private Date begin;
    /** 活动结束时间 */
    @Column(name = "end")
    private Date end;
    
}
