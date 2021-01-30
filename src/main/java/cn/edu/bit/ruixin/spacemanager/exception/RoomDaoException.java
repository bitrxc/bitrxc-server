package cn.edu.bit.ruixin.spacemanager.exception;

/**
 * 自定义的Room数据存取异常
 *
 * @author 78165
 * @date 2021/1/29
 */
public class RoomDaoException extends RuntimeException {
    public RoomDaoException() {super();}

    public RoomDaoException(String msg) {
        super(msg);
    }
}
