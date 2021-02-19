package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.community.domain.Schedule;
import cn.edu.bit.ruixin.community.repository.ScheduleRepository;
import cn.edu.bit.ruixin.community.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/10
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Override
    public List<Schedule> getAllTime() {
        return scheduleRepository.findAll();
    }
}
