package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/10
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Integer>, JpaSpecificationExecutor<Schedule> {

}
