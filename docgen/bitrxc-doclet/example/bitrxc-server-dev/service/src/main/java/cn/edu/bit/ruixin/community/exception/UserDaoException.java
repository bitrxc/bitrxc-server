package cn.edu.bit.ruixin.community.exception;

/**
 * 如果错误和用户的认证或查询过程有关，则抛出此异常
 * TODO
 *
 * @author 78165
 * @date 2021/2/5
 */
public class UserDaoException extends RuntimeException {
    public UserDaoException() {}

    public UserDaoException(String msg) {
        super(msg);
    }
}
