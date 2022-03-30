package cn.edu.bit.secondclass.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductInfoVo {
    /** 文创产品编号 */
    private int id;
    /** 文创产品名称 */
    private String name;
    /** 此文创产品的剩余库存 */
    private int stock;
    /** 兑换此文创产品所需要的积分 */
    private int point;
}
