package cn.edu.bit.secondclass.repository;

import cn.edu.bit.secondclass.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer>, JpaSpecificationExecutor<Activity> {
    Activity findActivityByNumber(String number);
    Activity findActivityById(int id);

    @Query(nativeQuery = true, value = "SELECT * FROM `activity`")
    List<Activity> findAll();
}
