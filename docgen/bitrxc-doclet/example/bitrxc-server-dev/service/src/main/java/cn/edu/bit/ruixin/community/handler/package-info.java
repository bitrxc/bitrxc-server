/**
 * <p>
 * 控制器、过滤器和入口点指的是一类没有内部状态的代码，这些代码定义了服务器对HTTP请求的响应。
 * </p><p>
 * 在响应HTTP请求的过程中，需要处理不匹配任何一个控制器的错误请求，通常由认证失败、格式或参数错误、动作无法执行等导致。
 * 此时框架会调用入口点对象（Entrypoint或handler）的方法来处理此类请求。
 * 入口点可以读取http请求的所有内容，必须调用响应组件来响应。
 * </p>
 * 
 * @author 78165
 * @author jingkaimori
 * @see cn.edu.bit.ruixin.base.security.utils.UnauthorizedEntryPoint
 */
package cn.edu.bit.ruixin.community.handler;
