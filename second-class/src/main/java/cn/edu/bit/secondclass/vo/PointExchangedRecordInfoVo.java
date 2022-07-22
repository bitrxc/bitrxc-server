package cn.edu.bit.secondclass.vo;

import cn.edu.bit.secondclass.domain.PointExchangedRecord;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@NoArgsConstructor
public class PointExchangedRecordInfoVo {
    /**  编号 **/
    private int id;
    /** 变动原因 **/
    private String reason;
    /** 兑换积分的学号 **/
    private String schoolId;
    /** 受理管理员 **/
    private Integer admin;
    /** 文创产品编号 **/
    private int product;
    /** 兑换产品的数量 **/
    private int quantity;
    /** 兑换状态 **/
    private String status;
    /** 兑换时间 **/
    private Date time;
    /** 审批留言 **/
    private String checkNote;

    public static PointExchangedRecordInfoVo convertToVo(PointExchangedRecord pointExchangedRecord) {
        PointExchangedRecordInfoVo infoVo = new PointExchangedRecordInfoVo();
        BeanUtils.copyProperties(pointExchangedRecord, infoVo);
        return infoVo;
    }

    public static PointExchangedRecord convertToPo(PointExchangedRecordInfoVo infoVo) {
        PointExchangedRecord record = new PointExchangedRecord();
        BeanUtils.copyProperties(infoVo, record);
        return record;
    }
}
