package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * @author blacknick
 * @date 2021/2/6
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Integer>, JpaSpecificationExecutor<Appointment> {
    Appointment findAppointmentById(Integer id);

    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `id` in (:ids)")
    List<Appointment> findAppointmentByIds(@Param("ids") List<Integer> ids);

    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `launcher` IS NULL")
    List<Appointment> findAppointmentAppointedByAdmin();

    @Query(nativeQuery = true, value = "SELECT  * FROM  `deal` WHERE `launcher` IS NULL AND `conductor` = :conductor AND `room` = :roomId AND `exec_date` = :execDate AND (`status` = :receive OR `status` = :executing)")
    List<Appointment> findAppointmentAppointedByAdminThroughAdminAndRoomIdAndExecDate(@Param("conductor") String conductor, @Param("roomId") Integer roomId, @Param("execDate") Date execDate, @Param("receive") String receive, @Param("executing") String executing);

    List<Appointment> findAllByLauncher(String username);

    List<Appointment> findAllByLauncherEqualsOrderByLaunchDate(String username);
//    Appointment findAppointmentByRoomIdEqualsAndExecDateEqualsAndLaunchTimeEqualsAndStatusEquals(Integer roomId, Date execDate, Integer launchTime, String status);

    Appointment findAppointmentByLauncherEqualsAndRoomIdEqualsAndExecDateEqualsAndLaunchTimeEqualsAndStatusEquals(String launcher, Integer roomId, Date execDate, Integer launchTime, String status);
    List<Appointment> findAllByStatusEqualsOrderByExecDateAscLaunchTimeAscLaunchDateAsc(String status);

//    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `room` = :roomId AND `exec_date` = :execDate AND `launch_time` = :launchTime AND (`status` = :receive OR `status` = :executing)")
//    Appointment findReceivedAppointment(@Param("roomId") Integer roomId, @Param("execDate") Date execDate, @Param("launchTime") Integer launchTime, @Param("receive") String receive, @Param("executing") String executing);

    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `room` = :roomId AND `exec_date` = :execDate AND ((`begin` >= :begin AND `begin` <= :end) OR (`end` >= :begin AND `end` <= :end )) AND (`status` = :receive OR `status` = :executing)")
    Appointment findReceivedAppointment(@Param("roomId") Integer roomId, @Param("execDate") Date execDate, @Param("begin") Integer begin, @Param("end") Integer end, @Param("receive") String receive, @Param("executing") String executing);

    // 查找与当前预约冲突的所有预约（包括管理员和普通用户的预约），需要该功能时应使用当前返回List的版本
    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `room` = :roomId AND `exec_date` = :execDate AND ((`begin` >= :begin AND `begin` <= :end) OR (`end` >= :begin AND `end` <= :end )) AND (`status` = :receive OR `status` = :executing)")
    List<Appointment> findConflictingAppointmentsByAdmin(@Param("roomId") Integer roomId, @Param("execDate") Date execDate, @Param("begin") Integer begin, @Param("end") Integer end, @Param("receive") String receive, @Param("executing") String executing);
//    @Query(nativeQuery = true, value = "SELECT `launch_time` FROM `deal` WHERE `room` = ? AND `status` = ?")
//    List<Integer> findLaunchTimeByRoomIdAndStatus(Integer roomId, String status);

    // 查找与当前预约冲突的普通用户的预约(普通用户预约的发起者不为空)
    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `launcher` IS NOT NULL AND `room` = :roomId AND `exec_date` = :execDate AND ((`begin` >= :begin AND `begin` <= :end) OR (`end` >= :begin AND `end` <= :end )) AND (`status` = :unreceived OR`status` = :receive OR `status` = :executing)")
    List<Appointment> findConflictingAppointmentsAppointedByUser(@Param("roomId") Integer roomId, @Param("execDate") Date execDate, @Param("begin") Integer begin, @Param("end") Integer end, @Param("unreceived") String unreceived, @Param("receive") String receive, @Param("executing") String executing);

    // 查找与当前预约冲突的管理员的预约(管理员预约的发起者为空，包括自己的预约)
    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `launcher` IS NULL AND `room` = :roomId AND `exec_date` = :execDate AND ((`begin` >= :begin AND `begin` <= :end) OR (`end` >= :begin AND `end` <= :end )) AND (`status` = :receive OR `status` = :executing)")
    List<Appointment> findConflictingAppointmentsAppointedByAdmin(@Param("roomId") Integer roomId, @Param("execDate") Date execDate, @Param("begin") Integer begin, @Param("end") Integer end, @Param("receive") String receive, @Param("executing") String executing);
//    @Query(nativeQuery = true, value = "SELECT `launch_time` FROM `deal` WHERE `room` = ? AND `status` = ?")
//    List<Integer> findLaunchTimeByRoomIdAndStatus(Integer roomId, String status);

    // 查找与当前房间冲突的管理员的预约(管理员预约的发起者为空，不包括自己的预约)
    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `launcher` IS NULL AND `room` = :roomId AND `conductor` != :conductor  AND `exec_date` = :execDate AND (`status` = :receive OR `status` = :executing)")
    List<Appointment> findConflictingAppointmentsAppointedByAdminThroughConductor(@Param("roomId") Integer roomId, @Param("conductor") String conductor, @Param("execDate") Date execDate, @Param("receive") String receive, @Param("executing") String executing);

    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `room` = :roomId AND `exec_date` = :execDate AND (`status` = :receive OR `status` = :executing)")
    List<Appointment> findLaunchTimeByRoomIdAndExecuteDateAndStatus(@Param("roomId") Integer roomId, @Param("execDate") Date execDate, @Param("receive") String receive, @Param("executing") String executing);

//    @Query(nativeQuery = true, value = "SELECT `launch_time` FROM `deal` WHERE `room` = :roomId AND `exec_date` = :execDate AND (`status` = :receive OR `status` = :executing)")
//    List<Integer> findLaunchTimeByRoomIdAndExecuteDateAndStatus(@Param("roomId") Integer roomId, @Param("execDate") Date execDate, @Param("receive") String receive, @Param("executing") String executing);


    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `room` = ? AND `launcher` = ? AND `exec_date` = ? AND `status` = ?")
    Appointment findLaunchTimeByRoomIdAndLauncherAndExecuteDateAndStatus(Integer roomId, String username, Date execDate, String status);

    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `launcher` = ? AND (`status` = ? OR `status` = ? OR `status` = ?)")
    Appointment findAppointmentByLauncherWithStatus(String launcher, String status1, String status2, String status3);

//    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `status` = :status ORDER BY `launch_date` DESC ")
    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `status` = :status ORDER BY `launch_date` DESC, `exec_date` DESC, `launch_time` DESC ")
    Page<Appointment> findAllPagesByStatus(@Param("status") String status, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `launcher` = :username ORDER BY `launch_date` DESC ")
//    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `launcher` = :username")
    Page<Appointment> findAllPagesByUsername(@Param("username") String username, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE NOT (`status` = :no1 OR `status` = :no2) ORDER BY `launch_date` DESC, `exec_date` DESC, `launch_time` DESC ")
    Page<Appointment> findAllPages(String no1, String no2, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM `deal` WHERE `room` = :roomId AND `status` = :status AND ((`begin` >= :begin AND `begin` <= :end) OR (`end` >= :begin AND `end` <= :end))")
    List<Appointment> getAppointmentsByRoomIdAndTimesAndStatus(Integer roomId, Integer begin, Integer end, String status);
}
