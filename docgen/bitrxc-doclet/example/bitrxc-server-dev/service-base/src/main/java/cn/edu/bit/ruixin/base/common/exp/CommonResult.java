package cn.edu.bit.ruixin.base.common.exp;

import lombok.Data;

import cn.edu.bit.ruixin.base.common.ResultCode;

/**
 * 统一返回结果封装（泛型化实验类）
 * 
 * 由于Java语言中，null可以被赋值给任何类型，因此不存在专门的Null类型。
 * 由于Void类型的唯一合理值为Null，故利用Void类型代替Null类型
 *
 * @author jingkaimori
 * @date 2022/1/20
 */
@Data
public class CommonResult<T> {
    /** 请求结果是否成功 */
    private Boolean success;
    /** 响应状态码 */
    private Integer code;
    /** 响应消息 */
    private String message;
    /** 响应结果数据 */
    private T data;

    private CommonResult(ResultCode resultCode) {
        this.success = resultCode.getSuccess();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = null;
        
    }

    /**
     * 
     * @param resClass data字段的类型，调用时必须提供
     * @return
     */
    public static <S> CommonResult<S> ok(Class<S> resClass) {
        CommonResult<S> result = new CommonResult<S>(ResultCode.SUCCESS);
        return result;
    }

    /**
     * 
     * @param resClass data字段的类型，调用时必须提供
     * @return
     */
    public static <S> CommonResult<S> errorCode(ResultCode resultCode,Class<S> resClass){
        CommonResult<S> result = new CommonResult<S>(resultCode);
        return result;
    }

    /**
     * 泛型对象作为返回值时的构造函数
     * @param <S> data字段的类型，调用时必须提供。由于运行时泛型类型会被抹除，因此无法通过class对象来推断泛型类的泛型成员
     * @return
     */
    public static <S> CommonResult<S> ok() {
        CommonResult<S> result = new CommonResult<S>(ResultCode.SUCCESS);
        return result;
    }

    /**
     * 泛型对象作为返回值时的构造函数
     * @param <S> data字段的类型，调用时必须提供。由于运行时泛型类型会被抹除，因此无法通过class对象来推断泛型类的泛型成员
     * @return
     */
    public static <S> CommonResult<S> errorCode(ResultCode resultCode){
        CommonResult<S> result = new CommonResult<S>(resultCode);
        return result;
    }

    public CommonResult<T> attribute(String key, Object value) 
        throws IllegalAccessException,NoSuchFieldException
    {
        this.data.getClass().getField(key).set(this.data,value);
        return this;
    }

    public CommonResult<T> data(T obj) {
        this.data = obj;
        return this;
    }

    public CommonResult<T> msg(String msg) {
        this.message = msg;
        return this;
    }
}
