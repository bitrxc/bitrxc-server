package cn.edu.bit.ruixin.spacemanager.repository;

import cn.edu.bit.ruixin.spacemanager.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/1/29
 */
/*
 JpaRepository<Room, Integer>封装了基本CRUD，JpaSpecificationExecutor<Room>封装了分页等复杂查询
 */
public interface RoomsRepository extends JpaRepository<Room, Integer>, JpaSpecificationExecutor<Room> {
    Room findRoomByName(String name);
    List<Room> findAllByNameLike(String name);
}
