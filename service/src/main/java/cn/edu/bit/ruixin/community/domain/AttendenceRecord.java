package cn.edu.bit.ruixin.community.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import javax.persistence.*;

/**
 * 用户出席某一次会议的记录
 * 
 */
@Entity
@Table(name = "attendence_record")
@Data
@NoArgsConstructor
public class AttendenceRecord {
    /** 参会记录Id，正常情况下用不到 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    /** 预约信息 */
    @OneToOne
    @JoinColumn(name = "meeting_id",referencedColumnName = "id")
    private Meeting meeting;
    /** 签到此活动的用户 */
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
    /** 用户签到时留下的附注 */
    @Column(name = "user_note")
    private String userNote;
    /** 用户签到时间 */
    @Column(name = "begin")
    private Date signDate;
    
}
