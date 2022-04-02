package cn.edu.bit.ruixin.community.service;

import java.util.List;

import cn.edu.bit.ruixin.community.vo.MeetingInfoVo;

/**
 * 和签到相关的服务
 * 
 * @author jingkaimori
 */
public interface MeetingService {
    
    void createMeeting(MeetingInfoVo meeting);

    void modifyMeeting(MeetingInfoVo meeting);

    void signToMeeting(int meetingid,int userid,String note);

    MeetingInfoVo getMeeting(int meetingid);

    List<MeetingInfoVo> getMeetingsByUsername(String username);
}
