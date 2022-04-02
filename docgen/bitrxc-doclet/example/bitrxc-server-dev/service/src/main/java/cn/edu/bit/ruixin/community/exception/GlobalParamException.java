package cn.edu.bit.ruixin.community.exception;

/**
 * 当数据转换时，数据结构不符合规范，则抛出此异常
 * TODO
 *
 * @author 78165
 * @date 2021/2/25
 */
public class GlobalParamException extends RuntimeException {
    public GlobalParamException() {
        super();
    }

    public GlobalParamException(String msg) {
        super(msg);
    }
}
