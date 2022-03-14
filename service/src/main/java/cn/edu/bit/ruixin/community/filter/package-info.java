/**
 * <p>
 * 控制器、过滤器和入口点指的是一类没有内部状态的代码，这些代码定义了服务器对HTTP请求的响应。
 * </p><p>
 * 本项目中，请求到达控制器前，会由过滤器（filter）来检查请求者的身份，并赋予该请求相应的权限。
 * 过滤器结束处理之前，必须通过调用函数的方法，将控制流导向其他过滤器
 * </p>
 * 
 * @author 78165
 * @author jingkaimori
 * @see https://shimo.im/docs/e1Az42LLOOcENEqW
 */
package cn.edu.bit.ruixin.community.filter;
