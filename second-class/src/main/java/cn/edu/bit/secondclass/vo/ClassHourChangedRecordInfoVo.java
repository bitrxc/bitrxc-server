package cn.edu.bit.secondclass.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClassHourChangedRecordInfoVo {
    private int id;
    private String schoolId;

    private String activity;

    private long date;

    private double variation;
}
