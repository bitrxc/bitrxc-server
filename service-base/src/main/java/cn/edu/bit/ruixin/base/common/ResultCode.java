package cn.edu.bit.ruixin.base.common;

/**
 * 返回结果枚举
 *
 * @author 78165
 * @date 2021/1/29
 */
public enum ResultCode {

    SUCCESS(true, 200, "操作成功！"),
    NOAHTHORITY(false, 400, "当前用户权限不足！"),
    INTERNAL_SERVER_ERROR(false, 500, "服务器内部异常!"),
    WECHATAUTHENTICATIONERROR(false, 500, "微信后台认证失败！请重试！"),
    UNAUTHENTICATION_ERROR(false, 401, "未登录！"),
    RESOURCE_NOT_FOUND(false, 404, "请求的资源不存在！");

    private Boolean success;
    private Integer code;
    private String message;

    private ResultCode(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
