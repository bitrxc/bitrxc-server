package cn.edu.bit.secondclass.repository;

import cn.edu.bit.secondclass.domain.PointExchangedRecord;
import org.graalvm.compiler.lir.alloc.lsra.LinearScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PointExchangedRecordRepository extends JpaRepository<PointExchangedRecord, Integer>
        , JpaSpecificationExecutor<PointExchangedRecord> {
    PointExchangedRecord findPointExchangedRecordById(int id);

    List<PointExchangedRecord> findPointExchangedRecordsByStatus(String status);

    List<PointExchangedRecord> findPointExchangedRecordsBySchoolId(String schoolId);

    List<PointExchangedRecord> findPointExchangedRecordsBySchoolIdAndStatus(String schoolId, String status);

    PointExchangedRecord findPointExchangedRecordBySchoolIdAndStatus(String schoolId, String status);
}
