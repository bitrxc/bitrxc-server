package cn.edu.bit.ruixin.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.edu.bit.ruixin.community.domain.AttendenceRecord;

/** */
public interface AttendenceRecordRepository extends JpaRepository<AttendenceRecord, Integer>, JpaSpecificationExecutor<AttendenceRecord> {
    
}