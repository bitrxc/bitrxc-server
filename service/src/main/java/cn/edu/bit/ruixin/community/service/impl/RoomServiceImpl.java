package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.base.security.utils.MapToBean;
import cn.edu.bit.ruixin.community.domain.Appointment;
import cn.edu.bit.ruixin.community.domain.Gallery;
import cn.edu.bit.ruixin.community.domain.Room;
import cn.edu.bit.ruixin.community.domain.Schedule;
import cn.edu.bit.ruixin.community.exception.GlobalParamException;
import cn.edu.bit.ruixin.community.myenum.AppointmentStatus;
import cn.edu.bit.ruixin.community.repository.AppointmentRepository;
import cn.edu.bit.ruixin.community.repository.GalleryRepository;
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

    @Autowired
    private GalleryRepository galleryRepository;

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
        String key = name+":"+id;
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
        if (redisService.opsForCheckKeyExist(Room.class.getName()+":"+room.getId())) {
            // 若缓存存在先清理缓存
            if (!redisService.opsForDeleteKey(Room.class.getName()+":"+room.getId())) {
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

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<Schedule> allTime = scheduleRepository.findAll();

        List<Schedule> pastTime = new ArrayList<>();

        List<Schedule> busyTime = new ArrayList<>();

        List<Schedule> freeTime = new ArrayList<>();

        List<Schedule> myTime = new ArrayList<>();

        int i = 0;

        for (i = 0; i < allTime.size(); i++) {
            String dateTime = date + " " + allTime.get(i).getBegin();
            Date execDateTime = null;
            try {
                execDateTime = dateTimeFormat.parse(dateTime);
            } catch (ParseException e) {
                throw new GlobalParamException("日期格式有误！");
            }
            if (execDateTime.before(nowDate)) {
                pastTime.add(allTime.get(i));
            } else {
                break;
            }
        }

        Date execDate = null;
        try {
            execDate = dateFormat.parse(date);
        } catch (ParseException e) {
            throw new GlobalParamException("日期格式有误！");
        }

        List<Appointment> appointments = appointmentRepository.findLaunchTimeByRoomIdAndExecuteDateAndStatus(roomId, execDate, AppointmentStatus.RECEIVE.getStatus(), AppointmentStatus.SIGNED.getStatus());

        // 获取所有占用时间段
        List<Integer> busyTimeId = new ArrayList<>();
        if (appointments!=null) {
            for (Appointment appointment :
                    appointments) {
                for (int j = appointment.getBegin(); j <= appointment.getEnd(); j++) {
                    busyTimeId.add(j);
                }
            }
            Collections.sort(busyTimeId);
        }

        for (int k = 0; k < busyTimeId.size() && i < allTime.size(); ) {
            if (busyTimeId.get(k) == allTime.get(i).getId()) {
                busyTime.add(allTime.get(i));
                k++;
                i++;
            } else if (busyTimeId.get(k) < allTime.get(i).getId()) {
                k++;
            } else {
                i++;
            }
        }


        Appointment appointment = appointmentRepository.findLaunchTimeByRoomIdAndLauncherAndExecuteDateAndStatus(roomId, username, execDate, AppointmentStatus.NEW.getStatus());
        ArrayList<Integer> myTimeId = new ArrayList<>();

        if (appointment != null) {
            for (int j = appointment.getBegin(); j <= appointment.getEnd() ; j++) {
                myTimeId.add(j);
            }
            for (Integer id :
                    myTimeId) {
                for (Schedule schedule:
                        allTime) {
                    if (schedule.getId() == id) {
                        myTime.add(schedule);
                    }
                }
            }

            // 需保证同一时间段被审批后，其余所有申请都驳回
            myTime = myTime.stream()
                    .filter(schedule -> (!pastTime.contains(schedule) && !busyTime.contains(schedule)))
                    .collect(Collectors.toList());
        }

        List<Schedule> finalMyTime = myTime;
        freeTime = allTime.stream()
                .filter(schedule -> (!pastTime.contains(schedule) && !busyTime.contains(schedule) && !finalMyTime.contains(schedule)))
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
        Date nowDate = new Date();

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<Schedule> allTime = scheduleRepository.findAll();
        // <时段,是否被预约> 标记被占用的时段
        Map<Integer, Boolean> scheduleMap = new HashMap<>();
        List<Schedule> passTime = allTime.stream()
                .filter(schedule -> {
                    String dateTime = date + " " + schedule.getBegin();
                    Date execDateTime = null;
                    try {
                        execDateTime = dateTimeFormat.parse(dateTime);
                    } catch (ParseException e) {
                        throw new GlobalParamException("日期格式有误！");
                    }
                    return execDateTime.before(nowDate);
                }).collect(Collectors.toList());

        Date execDate = null;
        try {
            execDate = dateFormat.parse(date);
        } catch (ParseException e) {
            throw new GlobalParamException("日期格式有误！");
        }

        //TODO 待重写Repository方法直接获取所需预约列表
        List<Appointment> conflictingAppointments = appointmentRepository
                .findAppointmentAppointedByAdmin()
                .stream()
                .filter(appointment -> appointment.getRoomId() == roomId && !appointment.getConductor().equals(conductor)
                        && (appointment.getStatus() == AppointmentStatus.RECEIVE.getStatus() || appointment.getStatus() == AppointmentStatus.SIGNED.getStatus()))
                .collect(Collectors.toList());


        conflictingAppointments.stream()
                .forEach(appointment -> {
                    scheduleMap.put(appointment.getBegin(), true);
                    scheduleMap.put(appointment.getEnd(), true);
                });
        List<Schedule> busyTime = allTime.stream()
                .filter(schedule -> scheduleMap.get(schedule.getId()))
                .collect(Collectors.toList());
        passTime.forEach(schedule -> scheduleMap.put(schedule.getId(), true));

        List<Appointment> adminAppointments = appointmentRepository.findAppointmentAppointedByAdminThroughAdmin(conductor);
        adminAppointments.forEach(appointment -> {
            scheduleMap.put(appointment.getBegin(), null);
            scheduleMap.put(appointment.getEnd(), null);
        });
        List<Schedule> myTime = allTime.stream()
                .filter(schedule -> !scheduleMap.get(schedule.getId()))
                .collect(Collectors.toList());
        adminAppointments.stream()
                .forEach(appointment -> {
                    scheduleMap.put(appointment.getBegin(), true);
                    scheduleMap.put(appointment.getEnd(), true);
                });

        List<Schedule> freeTime = allTime.stream()
                .filter(schedule -> !scheduleMap.get(schedule.getId()))
                .collect(Collectors.toList());

        Map<String, List<Schedule>> map = new HashMap<>();
        map.put("passTime", passTime);
        map.put("busyTime", busyTime);
        map.put("myTime", myTime);
        map.put("freeTime", freeTime);
        return map;
    }
}