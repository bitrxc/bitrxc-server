package cn.edu.bit.ruixin.community.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 用于传递 {@link Meeting} 类对象的视图层对象
 * </p><p>
 * 视图层对象是前端传来的数据包在Java上下文的表现形式。
 * spring框架内用于处理web请求的MVC框架，会根据视图层的类型定义，
 * 将前端发来的输入数据加工成为Java对象。
 * 视图层类型的类名，一般以{@literal Vo}结尾
 * </p><p>
 * 由于前端只能获取数据的整形ID，无法获取数据的引用，
 * 因此需要视图层对象来封装引用，并由可以访问仓库的服务
 * （{@link cn.edu.bit.ruixin.community.service}）来维护引用，
 * 不需要在此处定义转换为数据层对象的方法。
 * </p><p>
 * 由于在前后端传递数据的json语法，没有原生的日期定义，因此需要将日期
 * 转为长整型来传递
 * </p>
 * 
 * @author jingkaimori
 * @see Meeting
 * 
 */
@Data
@NoArgsConstructor
public class MeetingInfoVo {
    /** 活动ID，与房间预约ID相同 */
    private int id;

    /** 相关的房间预约信息 */
    private int appointmentId;
    /** 活动名称 */
    private String name;
    /** 活动介绍 */
    private String description;
    /** 发起此活动的用户 */
    private int launcherId;
    /** 活动开始时间 */
    private long begin;
    /** 活动结束时间 */
    private long end;
    
}
