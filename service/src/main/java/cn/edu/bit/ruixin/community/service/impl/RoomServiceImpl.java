package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.base.security.utils.MapToBean;
import cn.edu.bit.ruixin.community.domain.Appointment;
import cn.edu.bit.ruixin.community.domain.Room;
import cn.edu.bit.ruixin.community.domain.Schedule;
import cn.edu.bit.ruixin.community.exception.GlobalParamException;
import cn.edu.bit.ruixin.community.myenum.AppointmentStatus;
import cn.edu.bit.ruixin.community.repository.AppointmentRepository;
import cn.edu.bit.ruixin.community.repository.RoomsRepository;
import cn.edu.bit.ruixin.community.repository.ScheduleRepository;
import cn.edu.bit.ruixin.community.service.RedisService;
import cn.edu.bit.ruixin.community.service.RoomService;
import cn.edu.bit.ruixin.community.exception.RoomDaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

//import javax.validation.constraints.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/1/29
 */
@Service
public class RoomServiceImpl implements RoomService {

    //    Internal error occurred while executing "addNewRoom()" in class "RoomsManagerController".
    @Autowired
    private RoomsRepository roomsRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private RedisService redisService;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public Room addNewRoom(Room room) {
        if (roomsRepository.findRoomByName(room.getName()) != null) {
            throw new RoomDaoException("Room already exist!");
        }
        return roomsRepository.save(room);
    }

    @Override
    @Transactional(readOnly = true)
    public Room getRoomInfoById(Integer id) {
        // 先从redis缓存中查
        String name = Room.class.getName();
        String key = name + ":" + id;
        try {
            Room room = redisService.opsForHashGetAll(key, Room.class);

            if (room != null) {
                return room;
            }
            // 从数据库中查询并放入redis
            if (roomsRepository.findById(id).isPresent()) {
                room = roomsRepository.findById(id).get();
                redisService.opsForHashSetAll(key, MapToBean.beanToMap(room), 30, TimeUnit.DAYS);
                return room;

            } else {
                throw new RoomDaoException("该房间不存在!");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> getAllRoomList() {
        return roomsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Room> getRoomPages(Pageable pageable) {
        return roomsRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Room getRoomInfoByName(String name) {
        Room room = roomsRepository.findRoomByName(name);
        if (room != null) {
            return room;
        } else {
            throw new RoomDaoException("该房间不存在!");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Room> getRoomInfoByNameLike(String name) {
        return roomsRepository.findAllByNameLike(name);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void removeRoomById(Integer id) {
        if (roomsRepository.findById(id).isPresent()) {
            roomsRepository.deleteById(id);
        } else {
            throw new RoomDaoException("该房间不存在!");
        }
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public Room updateRoomInfoById(Room room) {
        if (redisService.opsForCheckKeyExist(Room.class.getName() + ":" + room.getId())) {
            // 若缓存存在先清理缓存
            if (!redisService.opsForDeleteKey(Room.class.getName() + ":" + room.getId())) {
                throw new RoomDaoException("修改房间失败！");
            }
        }
        if (roomsRepository.findById(room.getId()).isPresent()) {
            roomsRepository.findById(room.getId()).get();
            roomsRepository.save(room);
        } else {
            throw new RoomDaoException("该房间不存在!");
        }
        return room;
    }

//    @Override
//    @Transactional(readOnly = true)
//    public List<Schedule> getRoomFreeTime(Integer roomId) {
//        List<Integer> busy = appointmentRepository.findLaunchTimeByRoomIdAndStatus(roomId, "receive");
//        System.out.println(busy);
//        List<Schedule> allTime = scheduleRepository.findAll();
//        List<Schedule> freeTime = new ArrayList<>();
//        for (int i=0; i<allTime.size(); i++) {
//            boolean flag = true;
//            for (int j=0; j<busy.size(); j++) {
//                if (allTime.get(i).getId() == busy.get(j)) {
//                    flag = false;
//                    break;
//                }
//            }
//            if (flag) {
//                freeTime.add(allTime.get(i));
//            }
//        }
//        return freeTime;
//    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<Schedule>> getRoomFreeTime(Integer roomId, String username, String date) {

        Date nowDate = new Date();
        Date execDate = getExecDate(date);

        List<Schedule> allTime = scheduleRepository.findAll();

        List<Schedule> pastTime;
        List<Schedule> busyTime;
        List<Schedule> freeTime;
        List<Schedule> myTime;

        Set<Integer> passTimeId = new HashSet<>();
        Set<Integer>busyTimeId = new HashSet<>();
        Set<Integer>myTimeId = new HashSet<>();

        // 获取所有已过时间段
        pastTime = allTime.stream()
                .filter(schedule -> getExecDateTime(date, schedule.getEnd()).before(nowDate))
                .collect(Collectors.toList());

        pastTime.forEach(schedule -> passTimeId.add(schedule.getId()));

        // 某房间某日期的预约记录(预约状态：已预约未签到、已签到未签退)
        List<Appointment> appointments = appointmentRepository.findLaunchTimeByRoomIdAndExecuteDateAndStatus(roomId, execDate, AppointmentStatus.RECEIVE.getStatus(), AppointmentStatus.SIGNED.getStatus());

        appointments.forEach(appointment -> {
            int scheduleNum;
            if(username.equals(appointment.getLauncher())) {
                for(scheduleNum = appointment.getBegin(); scheduleNum <= appointment.getEnd(); scheduleNum++) {
                    myTimeId.add(scheduleNum);
                }
            } else { // 管理员预约(发起者为空)或其他用户预约(username与launcher不一致)
                for(scheduleNum = appointment.getBegin(); scheduleNum <= appointment.getEnd(); scheduleNum++) {
                    busyTimeId.add(scheduleNum);
                }
            }
        });

        // 获取被他人占用的时间段(不包含已过时间段)
        busyTime = allTime.stream()
                .filter(schedule -> busyTimeId.contains(schedule.getId())
                                && !passTimeId.contains(schedule.getId()))
                .collect(Collectors.toList());

        // 当前用户发起的未审批的预约
        Appointment myNewAppointment = appointmentRepository.findLaunchTimeByRoomIdAndLauncherAndExecuteDateAndStatus(roomId, username, execDate, AppointmentStatus.NEW.getStatus());
        if(myNewAppointment != null) {
            for(int scheduleIdNum = myNewAppointment.getBegin(); scheduleIdNum <= myNewAppointment.getEnd(); scheduleIdNum++) {
                myTimeId.add(scheduleIdNum);
            }
        }

        // 当前用户预约的时间段(状态：未审批、已预约未签到、已签到，且不包含已过时间段)
        myTime = allTime.stream()
                .filter(schedule -> myTimeId.contains(schedule.getId())
                                && !passTimeId.contains(schedule.getId())
                                && !busyTimeId.contains(schedule.getId()))
                .collect(Collectors.toList());

        freeTime = allTime.stream()
                .filter(schedule -> !myTimeId.contains(schedule.getId())
                                && !passTimeId.contains(schedule.getId())
                                && !busyTimeId.contains(schedule.getId()))
                .collect(Collectors.toList());

        Map<String, List<Schedule>> map = new HashMap<>();
        map.put("passTime", pastTime);
        map.put("busyTime", busyTime);
        map.put("myTime", myTime);
        map.put("freeTime", freeTime);

        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<Schedule>> getRoomFreeTimeByAdmin(Integer roomId, String conductor, String date) {

        Date execDate = getExecDate(date);
        Date nowDate = new Date();

        List<Schedule> allTime = scheduleRepository.findAll();
        List<Schedule> passTime;
        List<Schedule> busyTime;
        List<Schedule> freeTime;
        List<Schedule> myTime;

        // <时段,是否被预约> 标记被占用的时段
        Map<Integer, Boolean> scheduleMap = new HashMap<>();

        // 获取已过时间段列表
        passTime = allTime.stream()
                .filter(schedule -> {
                    Date execDateTime = getExecDateTime(date, schedule.getEnd());
                    return execDateTime.before(nowDate);
                }).collect(Collectors.toList());

        passTime.forEach(schedule -> scheduleMap.put(schedule.getId(), false));

        // 其他管理员的预约(不包括当前管理员)
        List<Appointment> conflictingAppointments = appointmentRepository.findConflictingAppointmentsAppointedByAdminThroughConductor(roomId, conductor, execDate, AppointmentStatus.RECEIVE.getStatus(), AppointmentStatus.SIGNED.getStatus());

        conflictingAppointments.forEach(appointment -> {
            for(int i = appointment.getBegin(); i <= appointment.getEnd(); i++) {
                scheduleMap.putIfAbsent(i, true); // 排除已过时间段
            }
        });

        busyTime = allTime.stream()
                .filter(schedule -> scheduleMap.get(schedule.getId()) != null && scheduleMap.get(schedule.getId()))
                .collect(Collectors.toList());

        // 管理员自己占用的时间段(不包含已过时间段)
        passTime.forEach(schedule -> scheduleMap.put(schedule.getId(), true));
        List<Appointment> adminAppointments = appointmentRepository.findAppointmentAppointedByAdminThroughAdminAndRoomIdAndExecDate(conductor, roomId, execDate, AppointmentStatus.RECEIVE.getStatus(), AppointmentStatus.SIGNED.getStatus());
        adminAppointments.forEach(appointment -> {
            for(int i = appointment.getBegin(); i <= appointment.getEnd(); i++) {
                scheduleMap.putIfAbsent(i, false); // 排除已过时间段
            }
        });
        myTime = allTime.stream()
                .filter(schedule -> scheduleMap.get(schedule.getId()) != null && !scheduleMap.get(schedule.getId()))
                .collect(Collectors.toList());

        // 可用时间段
        freeTime = allTime.stream()
                .filter(schedule -> scheduleMap.get(schedule.getId()) == null)
                .collect(Collectors.toList());

        Map<String, List<Schedule>> map = new HashMap<>();
        map.put("passTime", passTime);
        map.put("busyTime", busyTime);
        map.put("myTime", myTime);
        map.put("freeTime", freeTime);
        return map;
    }

    /**
     * 将指定格式的日期字符串转换为Date对象
     * @param dateStr
     * @return
     * @throws GlobalParamException
     */
    private Date getExecDate(String dateStr) throws GlobalParamException {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (ParseException e) {
            throw new GlobalParamException("日期格式有误！");
        }
    }

    /**
     * 根据日期和时间字符串生成Date对象
     * @param dateStr
     * @param timeStr
     * @return
     * @throws GlobalParamException
     */
    private Date getExecDateTime(String dateStr, String timeStr) throws GlobalParamException {
        String dateTimeStr = dateStr + " " + timeStr;
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTimeStr);
        } catch (ParseException e) {
            throw new GlobalParamException("时间格式有误！");
        }
    }
}
