package cn.edu.bit.secondclass.service.impl;

import cn.edu.bit.secondclass.domain.Activity;
import cn.edu.bit.secondclass.repository.ActivityRepository;
import cn.edu.bit.secondclass.service.ActivityService;
import cn.edu.bit.secondclass.vo.ActivityInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public List<ActivityInfoVo> getAllActivities() {
        return activityRepository
                .findAll()
                .stream()
                .map(ActivityServiceImpl::convertActivityToVo)
                .collect(Collectors.toList());
    }

    @Override
    public ActivityInfoVo getActivityByNumber(String number) {
        return convertActivityToVo(activityRepository.findActivityByNumber(number));
    }

    @Override
    public ActivityInfoVo getActivityById(int id) {
        return convertActivityToVo(activityRepository.findActivityById(id));
    }

    private static ActivityInfoVo convertActivityToVo(Activity activity) {
        ActivityInfoVo infoVo = new ActivityInfoVo();
        infoVo.setId(activity.getId());
        infoVo.setNumber(activity.getNumber());
        infoVo.setPrimaryCategory(activity.getPrimaryCategory());
        infoVo.setSecondCategory(activity.getSecondCategory());
        infoVo.setInfo(activity.getInfo());
        return infoVo;
    }
}
