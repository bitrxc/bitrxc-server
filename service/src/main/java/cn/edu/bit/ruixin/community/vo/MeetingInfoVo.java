package cn.edu.bit.ruixin.community.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于传递 {@link Meeting} 类对象的视图层对象
 * 
 * @author jingkaimori
 * @see Meeting
 * 
 */
@Data
@NoArgsConstructor
public class MeetingInfoVo {
    /** 活动ID，与房间预约ID相同 */
    private int id;

    /** 相关的房间预约信息 */
    private int appointmentId;
    /** 活动名称 */
    private String name;
    /** 活动介绍 */
    private String description;
    /** 发起此活动的用户 */
    private int launcherId;
    /** 活动开始时间 */
    private long begin;
    /** 活动结束时间 */
    private long end;
    
}
