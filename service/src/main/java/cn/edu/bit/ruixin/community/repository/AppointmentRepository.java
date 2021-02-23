package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/6
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Integer>, JpaSpecificationExecutor<Appointment> {
    Appointment findAppointmentById(Integer id);
    List<Appointment> findAllByLauncher(String username);
    Appointment findAppointmentByRoomIdEqualsAndLaunchTimeEqualsAndStatusEquals(Integer roomId, Integer launchTime, String status);
    List<Appointment> findAllByStatusEquals(String status);

    @Query(nativeQuery = true, value = "SELECT `launch_time` FROM `deal` WHERE `room` = ? AND `status` = ?")
    List<Integer> findLaunchTimeByRoomIdAndStatus(Integer roomId, String status);
}
