package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
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
    List<Appointment> findAllByLauncherEqualsOrderByLaunchDate(String username);
//    Appointment findAppointmentByRoomIdEqualsAndExecDateEqualsAndLaunchTimeEqualsAndStatusEquals(Integer roomId, Date execDate, Integer launchTime, String status);

    Appointment findAppointmentByLauncherEqualsAndRoomIdEqualsAndExecDateEqualsAndLaunchTimeEqualsAndStatusEquals(String launcher, Integer roomId, Date execDate, Integer launchTime, String status);
    List<Appointment> findAllByStatusEqualsOrderByExecDateAscLaunchTimeAscLaunchDateAsc(String status);

//    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `room` = :roomId AND `exec_date` = :execDate AND `launch_time` = :launchTime AND (`status` = :receive OR `status` = :executing)")
//    Appointment findReceivedAppointment(@Param("roomId") Integer roomId, @Param("execDate") Date execDate, @Param("launchTime") Integer launchTime, @Param("receive") String receive, @Param("executing") String executing);

    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `room` = :roomId AND `exec_date` = :execDate AND ((`begin` >= :begin AND `begin` <= :end) OR (`end` >= :begin AND `end` <= :end )) AND (`status` = :receive OR `status` = :executing)")
    Appointment findReceivedAppointment(@Param("roomId") Integer roomId, @Param("execDate") Date execDate, @Param("begin") Integer begin, @Param("end") Integer end, @Param("receive") String receive, @Param("executing") String executing);


    @Query(nativeQuery = true, value = "SELECT `launch_time` FROM `deal` WHERE `room` = ? AND `status` = ?")
    List<Integer> findLaunchTimeByRoomIdAndStatus(Integer roomId, String status);

    @Query(nativeQuery = true, value = "SELECT `begin`, `end` FROM `deal` WHERE `room` = :roomId AND `exec_date` = :execDate AND (`status` = :receive OR `status` = :executing)")
    List<Appointment> findLaunchTimeByRoomIdAndExecuteDateAndStatus(@Param("roomId") Integer roomId, @Param("execDate") Date execDate, @Param("receive") String receive, @Param("executing") String executing);

//    @Query(nativeQuery = true, value = "SELECT `launch_time` FROM `deal` WHERE `room` = :roomId AND `exec_date` = :execDate AND (`status` = :receive OR `status` = :executing)")
//    List<Integer> findLaunchTimeByRoomIdAndExecuteDateAndStatus(@Param("roomId") Integer roomId, @Param("execDate") Date execDate, @Param("receive") String receive, @Param("executing") String executing);


    @Query(nativeQuery = true, value = "SELECT `begin`, `end` FROM `deal` WHERE `room` = ? AND `launcher` = ? AND `exec_date` = ? AND `status` = ?")
    Appointment findLaunchTimeByRoomIdAndLauncherAndExecuteDateAndStatus(Integer roomId, String username, Date execDate, String status);

    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `launcher` = ? AND `status` = ?")
    Appointment findAppointmentByLauncherWithStatus(String launcher, String status);
}
