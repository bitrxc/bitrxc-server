package cn.edu.bit.secondclass.vo;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserInfoVo {
    /** 用户id */
    private int id;
    /** 用户积分 */
    private int point;
    /** 总学时 */
    private double classHour;
    /** 主题讲座学时 */
    private double lectureHour;
    /** 用户学号 */
    private String schoolId;
}
