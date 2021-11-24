package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.community.domain.Room;
import cn.edu.bit.ruixin.community.domain.Schedule;
import cn.edu.bit.ruixin.community.service.RedisService;
import cn.edu.bit.ruixin.community.service.RoomService;
import cn.edu.bit.ruixin.community.service.ScheduleService;
import cn.edu.bit.ruixin.community.vo.RoomInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
 * @date 2021/1/29
 */
@RestController
@RequestMapping("/room")
@CrossOrigin
public class RoomController {

    @Autowired
    private RoomService roomService;

    private final ReadWriteLock readWriteLock;

    private final Lock readLock;

    @Autowired
    public RoomController(ReadWriteLock readWriteLock) {
        this.readWriteLock = readWriteLock;
        this.readLock = readWriteLock.readLock();
    }

    /**
     * 根据房间ID查询
     * @param id
     * @return
     */
    /**
     * @api {Get} /room/{id} 根据Id查询房间信息
     * @apiGroup test
     * @apiDescription 根据Id查询房间信息
     * @apiSuccess {RoomInfoVo} roomInfo data携带"rooms"，是对象数组形式，每个元素是一个房间信息
     */

    /**
     * @api {Get} /room 获取所有房间信息
     * @apiGroup test
     * @apiDescription 获取所有房间信息
     * @apiSuccess {RoomInfoVo} roomInfo data携带"roomInfo"
     */
    @GetMapping("/{id}")
    public CommonResult getRoomInfoById(@PathVariable("id") Integer id) {
        Room room = roomService.getRoomInfoById(id);
        return CommonResult.ok(ResultCode.SUCCESS).data("roomInfo", RoomInfoVo.convertToVo(room));
    }

    /**
     * 查询所有房间
     * @return
     */
    @GetMapping("")
    public CommonResult getAllRoomList() {
        List<Room> list = roomService.getAllRoomList();
        List<RoomInfoVo> infoVos = new ArrayList<>();
        for (Room room :
                list) {
            infoVos.add(RoomInfoVo.convertToVo(room));
        }
        return CommonResult.ok(ResultCode.SUCCESS).data("rooms", infoVos);
    }

    /**
     * 分页查询
     * @param current
     * @param limit
     * @return
     */
    @GetMapping("/{current}/{limit}")
    public CommonResult getRoomPages(@PathVariable("current") int current, @PathVariable("limit") int limit) {
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
    }

    /**
     * @api {Get} /room/name 根据房间名精确查询房间信息
     * @apiGroup test
     * @apiDescription 根据房间名精确查询房间信息
     * @apiParam {String} name 房间名
     */
    @GetMapping("/name")
    public CommonResult getRoomByName(@RequestParam("name")String name) {
        Room room = roomService.getRoomInfoByName(name);
        return CommonResult.ok(ResultCode.SUCCESS).data("roomInfo", RoomInfoVo.convertToVo(room));
    }

    /**
     * @api {Get} /room/nameLike 根据房间名模糊查询
     * @apiGroup test
     * @apiDescription 根据房间名模糊查询
     * @apiParam {String} name 房间名
     */
    @GetMapping("/nameLike")
    public CommonResult getRoomByNameLike(@RequestParam("nameLike")String name) {
        List<Room> list = roomService.getRoomInfoByNameLike("%" + name + "%");
        List<RoomInfoVo> infoVos = new ArrayList<>();
        for (Room room :
                list) {
            infoVos.add(RoomInfoVo.convertToVo(room));
        }
        return CommonResult.ok(ResultCode.SUCCESS).data("rooms", infoVos);
    }

    /**
     * @api {Get} /free/time 根据房间获取可用时段
     * @apiGroup test
     * @apiDescription 根据房间获取可用时段
     * @apiParam {Integer} roomId 房间ID
     * @apiParam {String} username wxid
     * @apiParam {String} date 要预约的时间段的当天日期，字符串形式：2021-02-26
     * @apiSuccess {Array} data 返回结果包括当天已过去时间段、空闲时间段、已被占据时间段、我已发起申请时间段
     * @apiSuccessExample {json} 响应数据示例
     * {
     *     "success": true,
     *     "code": 200,
     *     "message": "操作成功",
     *     "data": {
     *         "passTime": [
     *             {
     *                 "id": 1,
     *                 "begin": "07:00:00",
     *                 "end": "08:00:00"
     *             },
     *             {
     *                 "id": 2,
     *                 "begin": "08:00:00",
     *                 "end": "09:00:00"
     *             },
     *             {
     *                 "id": 3,
     *                 "begin": "09:00:00",
     *                 "end": "10:00:00"
     *             }
     *         ],
     *         "freeTime": [
     *             {
     *                 "id": 4,
     *                 "begin": "13:00:00",
     *                 "end": "14:00:00"
     *             }
     *         ],
     *         "myTime": [
     *             {
     *                 "id": 6,
     *                 "begin": "21:00:00",
     *                 "end": "22:00:00"
     *             }
     *         ],
     *         "busyTime": [
     *             {
     *                 "id": 5,
     *                 "begin": "14:00:00",
     *                 "end": "15:00:00"
     *             }
     *         ]
     *     }
     * }
     */
    @GetMapping("/free/time")
    public CommonResult getFreeTimeByRoomId(@RequestParam("roomId") Integer roomId, @RequestParam("username") String username, @RequestParam("date") String date) {
        Map map = roomService.getRoomFreeTime(roomId, username, date);
        return CommonResult.ok(ResultCode.SUCCESS).data(map);
    }
}
