package cn.edu.bit.ruixin.community.exception;

/**
 * 查询或操作的资源不存在时，抛出此异常
 * 
 * @author jingkaimori
 * @date 2022/3/30
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String msg){
        super(msg);
    }

    public ResourceNotFoundException(String msg,Throwable reason){
        super(msg,reason);
    }
}
