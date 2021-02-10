package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.community.domain.Room;
import cn.edu.bit.ruixin.community.domain.Schedule;
import cn.edu.bit.ruixin.community.repository.AppointmentRepository;
import cn.edu.bit.ruixin.community.repository.RoomsRepository;
import cn.edu.bit.ruixin.community.repository.ScheduleRepository;
import cn.edu.bit.ruixin.community.service.RoomService;
import cn.edu.bit.ruixin.community.exception.RoomDaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public Room addNewRoom(Room room) {
        if (roomsRepository.findRoomByName(room.getName()) != null) {
            throw new RoomDaoException("Room already exist!");
        }
        return roomsRepository.save(room);
    }

    @Override
    @Transactional(readOnly = true)
    public Room getRoomInfoById(Integer id) {
        if (roomsRepository.findById(id).isPresent()) {
            return roomsRepository.findById(id).get();
        } else {
            throw new RoomDaoException("该房间不存在!");
        }
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
        if (roomsRepository.findById(room.getId()).isPresent()) {
            roomsRepository.findById(room.getId()).get();
            roomsRepository.save(room);
        } else {
            throw new RoomDaoException("该房间不存在!");
        }
        return room;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Schedule> getRoomFreeTime(Integer roomId) {
        List<Integer> busy = appointmentRepository.findLaunchTimeByRoomIdAndStatus(roomId, "receive");
        System.out.println(busy);
        List<Schedule> allTime = scheduleRepository.findAll();
        List<Schedule> freeTime = new ArrayList<>();
        for (int i=0; i<allTime.size(); i++) {
            boolean flag = true;
            for (int j=0; j<busy.size(); j++) {
                if (allTime.get(i).getId() == busy.get(j)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                freeTime.add(allTime.get(i));
            }
        }
        return freeTime;
    }
}
