package cn.edu.bit.secondclass.service;

import cn.edu.bit.secondclass.domain.PointChangedRecord;
import cn.edu.bit.secondclass.domain.PointExchangedRecord;
import cn.edu.bit.secondclass.vo.PointExchangedRecordInfoVo;

import java.awt.*;
import java.util.List;

public interface PointExchangedRecordService {
    List<PointExchangedRecordInfoVo> getAllPointExchangedRecords(String status);

    List<PointExchangedRecordInfoVo> getPointExchangedRecordByUserIdAndStatus(String schoolId, String status);

    void checkout(int recordId, String status, Integer adminId, String checkNote);
}
