package cn.edu.bit.secondclass.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
public class ActivityInfoVo {

    private int id;

    private String number;

    private String primaryCategory;

    private String secondCategory;

    private String info;
}
