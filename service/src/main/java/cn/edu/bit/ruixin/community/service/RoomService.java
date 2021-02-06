package cn.edu.bit.ruixin.community.service;

import cn.edu.bit.ruixin.community.domain.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/1/29
 */
public interface RoomService {
    Room addNewRoom(Room room);

    Room getRoomInfoById(Integer id);

    List<Room> getAllRoomList();

    Page<Room> getRoomPages(Pageable pageable);

    Room getRoomInfoByName(String name);

    List<Room> getRoomInfoByNameLike(String name);

    void removeRoomById(Integer id);

    Room updateRoomInfoById(Room room);
}
