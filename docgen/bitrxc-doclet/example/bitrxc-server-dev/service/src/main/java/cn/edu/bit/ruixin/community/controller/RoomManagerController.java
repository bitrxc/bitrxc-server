package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.community.annotation.MsgSecCheck;
import cn.edu.bit.ruixin.community.domain.Room;
import cn.edu.bit.ruixin.community.service.ImagesService;
import cn.edu.bit.ruixin.community.service.RoomService;
import cn.edu.bit.ruixin.community.vo.RoomInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/2/26
 */
@CrossOrigin
@RestController
@RequestMapping("/admin/room")
public class RoomManagerController {


    @Autowired
    private RoomService roomService;

    @Autowired
    private ImagesService imagesService;

    private final ReadWriteLock readWriteLock;

    private final Lock writeLock;

    private final Lock readLock;

    @Autowired
    public RoomManagerController(ReadWriteLock readWriteLock) {
        this.readWriteLock = readWriteLock;
        this.readLock = readWriteLock.readLock();
        this.writeLock = readWriteLock.writeLock();
    }

    @PreAuthorize("hasAuthority('room')")
    @MsgSecCheck("infoVo")
    @PostMapping("")
    public CommonResult addRoom(@RequestBody(required = true) RoomInfoVo infoVo) {
        Room room = roomService.addNewRoom(RoomInfoVo.convertToPo(infoVo));
        return CommonResult.ok(ResultCode.SUCCESS).data("roomInfo", RoomInfoVo.convertToVo(room));
    }

    /**
     * 先删除缓存中数据，后删除数据库，加写锁
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('room')")
    @DeleteMapping("/{id}")
    public CommonResult deleteRoomById(@PathVariable("id") Integer id) {
        // 保证缓存中数据一致性，使用写锁，防止在写操作完成前，读操作更新了缓存
        writeLock.lock();
        try {
            roomService.removeRoomById(id);
            return CommonResult.ok(ResultCode.SUCCESS);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 修改数据要加写锁，保证缓存中数据一致性
     * @param infoVo
     * @return
     */
    @PreAuthorize("hasAuthority('room')")
    @MsgSecCheck("infoVo")
    @PutMapping("")
    public CommonResult updateRoomInfoById(@RequestBody(required = true) RoomInfoVo infoVo) {
        writeLock.lock();
        try {
            Room room = roomService.updateRoomInfoById(RoomInfoVo.convertToPo(infoVo));
            return CommonResult.ok(ResultCode.SUCCESS).data("roomInfo", RoomInfoVo.convertToVo(room));
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 读操作加读锁
     * 根据房间ID查询
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('room')")
    @GetMapping("/{id}")
    public CommonResult getRoomInfoById(@PathVariable("id") Integer id) {
        readLock.lock();
        try {
            Room room = roomService.getRoomInfoById(id);
            Map<String, Object> map = new HashMap<>();
            map.put("id", room.getId());
            map.put("name", room.getName());
            map.put("description", room.getDescription());
            map.put("images", room.getImage());
            return CommonResult.ok(ResultCode.SUCCESS).data("roomInfo", map);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 查询所有房间
     * @return
     */
    @PreAuthorize("hasAuthority('basicView')")
    @GetMapping("")
    public CommonResult getAllRoomList() {
        readLock.lock();
        try {
            List<Room> list = roomService.getAllRoomList();
            List<RoomInfoVo> infoVos = new ArrayList<>();
            for (Room room :
                    list) {
                infoVos.add(RoomInfoVo.convertToVo(room));
            }
            return CommonResult.ok(ResultCode.SUCCESS).data("rooms", infoVos);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 分页查询
     * @param current
     * @param limit
     * @return
     */
    @PreAuthorize("hasAuthority('basicView')")
    @GetMapping("/{current}/{limit}")
    public CommonResult getRoomPages(@PathVariable("current") int current, @PathVariable("limit") int limit) {
        readLock.lock();
        try {
            // 构造分页对象
            Pageable pageable = PageRequest.of(current, limit);
            Page<Room> page = roomService.getRoomPages(pageable);
            List<Room> list = page.getContent();
            List<RoomInfoVo> infoVos = new ArrayList<>();
            for (Room room :
                    list) {
                infoVos.add(RoomInfoVo.convertToVo(room));
            }
            Map<String, Object> map = new HashMap<>();
            map.put("totalElements", page.getTotalElements());
            map.put("totalPages", page.getTotalPages());
            map.put("hasNext", page.hasNext());
            map.put("hasPrevious", page.hasPrevious());
            map.put("rooms", infoVos);
            return CommonResult.ok(ResultCode.SUCCESS).data(map);
        } finally {
            readLock.unlock();
        }
    }

    @PreAuthorize("hasAuthority('room')")
    @GetMapping("/name")
    public CommonResult getRoomByName(@RequestParam("name")String name) {
        readLock.lock();
        try {
            Room room = roomService.getRoomInfoByName(name);
            return CommonResult.ok(ResultCode.SUCCESS).data("roomInfo", RoomInfoVo.convertToVo(room));
        } finally {
            readLock.unlock();
        }
    }

    @PreAuthorize("hasAuthority('room')")
    @GetMapping("/nameLike")
    public CommonResult getRoomByNameLike(@RequestParam("nameLike")String name) {
        readLock.lock();
        try {
            List<Room> list = roomService.getRoomInfoByNameLike("%" + name + "%");
            List<RoomInfoVo> infoVos = new ArrayList<>();
            for (Room room :
                    list) {
                infoVos.add(RoomInfoVo.convertToVo(room));
            }
            return CommonResult.ok(ResultCode.SUCCESS).data("rooms", infoVos);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 不涉及缓存不用加锁
     * @param roomId
     * @param username
     * @param date
     * @return
     */
    @PreAuthorize("hasAuthority('room')")
    @GetMapping("/free/time")
    public CommonResult getFreeTimeByRoomId(@RequestParam("roomId") Integer roomId, @RequestParam("username") String username, @RequestParam("date") String date) {
        Map map = roomService.getRoomFreeTime(roomId, username, date);
        return CommonResult.ok(ResultCode.SUCCESS).data(map);
    }

}
