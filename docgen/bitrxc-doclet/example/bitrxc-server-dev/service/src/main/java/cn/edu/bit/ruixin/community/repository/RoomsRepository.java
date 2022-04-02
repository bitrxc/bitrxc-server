package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/1/29
 */
public interface RoomsRepository extends JpaRepository<Room, Integer>, JpaSpecificationExecutor<Room> {
    Room findRoomByName(String name);
    List<Room> findAllByNameLike(String name);
//    void addOne(Room room);
}
