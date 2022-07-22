package cn.edu.bit.secondclass.service;

import cn.edu.bit.secondclass.domain.Activity;
import cn.edu.bit.secondclass.vo.ActivityInfoVo;

import java.util.List;

public interface ActivityService {
    List<ActivityInfoVo> getAllActivities();
    ActivityInfoVo getActivityByNumber(String number);
    ActivityInfoVo getActivityById(int id);
}
