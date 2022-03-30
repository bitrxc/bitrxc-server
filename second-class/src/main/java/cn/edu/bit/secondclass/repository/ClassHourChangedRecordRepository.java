package cn.edu.bit.secondclass.repository;

import cn.edu.bit.secondclass.domain.ClassHourChangedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassHourChangedRecordRepository extends JpaRepository<ClassHourChangedRecord, Integer>
        , JpaSpecificationExecutor<ClassHourChangedRecord> {

}
