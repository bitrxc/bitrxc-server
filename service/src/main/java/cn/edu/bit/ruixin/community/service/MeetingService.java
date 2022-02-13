package cn.edu.bit.ruixin.community.service;

import java.util.List;

import cn.edu.bit.ruixin.community.vo.MeetingInfoVo;

/**
 * <p>
 * 和签到相关的服务
 * </p><p>
 * 在spring框架的语义中，服务（service）指的是一类具有副作用的代码。
 * 不同于一些工具函数，服务可以不经过调用方的授权，访问持久化存储仓库以及其他服务。
 * 避免服务的调用方（本项目中大多数为控制器）访问仓库
 * 大多数情况下，service类的对象不能由调用方初始化，而需要采用依赖注入的方式，
 * 由spring框架将全局唯一的服务对象注入到调用方的上下文当中
 * </p>
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
