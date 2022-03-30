package cn.edu.bit.secondclass.repository;

import cn.edu.bit.secondclass.domain.PointChangedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PointChangedRecordRepository extends JpaRepository<PointChangedRecord, Integer>, JpaSpecificationExecutor<PointChangedRecord> {
    PointChangedRecord findPointChangedRecordById(int id);
}
