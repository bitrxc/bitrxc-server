package cn.edu.bit.ruixin.base.common.exp;

import lombok.Data;

import cn.edu.bit.ruixin.base.common.ResultCode;

/**
 * 统一返回结果封装（泛型化实验类）
 * TODO 区分Object 根类型和Null类型
 * 由于Java语言中，null可以为任何类型，因此不存在专门的Null类型，文档中使用Object类型代替Null类型
 *
 * @author jingkaimori
 * @date 2022/1/20
 */
@Data
public class CommonResult<T> {
//    @ApiModelProperty("请求结果是否成功")
    private Boolean success;
//    @ApiModelProperty("请求响应状态码")
    private Integer code;
//    @ApiModelProperty("响应消息")
    private String message;
//    @ApiModelProperty("响应结果数据")
    private T data;

    private CommonResult(ResultCode resultCode) {
        this.success = resultCode.getSuccess();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = null;
        
    }

    public static <S> CommonResult<S> ok(Class<S> resClass) {
        CommonResult<S> result = new CommonResult<S>(ResultCode.SUCCESS);
        return result;
    }

    public static <S> CommonResult<S> errorCode(ResultCode resultCode,Class<S> resClass){
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
