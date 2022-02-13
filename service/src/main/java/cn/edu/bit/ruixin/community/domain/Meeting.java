package cn.edu.bit.ruixin.community.domain;

import lombok.Data;

import java.util.Date;

import javax.persistence.*;

/**
 * 会议信息
 * 
 * 数据库字段对象，是JDBC引擎对数据库表的封装，{@link cn.edu.bit.ruixin.community.domain}包内
 * 原有的字段对象当中 很大一部分本身就是视图层对象 此类对象使用id来标识其他字段对象
 * 2022年以后创建的对象当中 大多数利用JDBC的映射规则直接将id转化为对象
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
