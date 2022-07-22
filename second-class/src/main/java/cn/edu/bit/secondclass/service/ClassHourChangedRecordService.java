package cn.edu.bit.secondclass.service;

import cn.edu.bit.secondclass.vo.ClassHourChangedRecordInfoVo;

import java.util.List;

public interface ClassHourChangedRecordService {
    void updateUserClassHour(List<ClassHourChangedRecordInfoVo> recordInfoVos, Integer adminId, String reason);
}
