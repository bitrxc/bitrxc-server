package cn.edu.bit.ruixin.spacemanager.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一返回结果封装
 *
 * @author 78165
 * @date 2021/1/29
 */
@Data
public class CommonResult {
//    @ApiModelProperty("请求结果是否成功")
    private Boolean success;
//    @ApiModelProperty("请求响应状态码")
    private Integer code;
//    @ApiModelProperty("响应消息")
    private String message;
//    @ApiModelProperty("响应结果数据")
    private Map<String, Object> data = new HashMap<>();

    private CommonResult(ResultCode resultCode) {
        this.success = resultCode.getSuccess();
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public static CommonResult ok(ResultCode resultCode) {
        CommonResult result = new CommonResult(resultCode);
        return result;
    }

    public static CommonResult error(ResultCode resultCode) {
        CommonResult result = new CommonResult(resultCode);
        return result;
    }

    public CommonResult data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public CommonResult data(Map<String, Object> map) {
        this.data = map;
        return this;
    }

    public CommonResult msg(String msg) {
        this.message = msg;
        return this;
    }
}
