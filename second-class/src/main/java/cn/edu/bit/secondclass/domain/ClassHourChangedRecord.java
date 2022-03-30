package cn.edu.bit.secondclass.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "class_hour_changed_record")
public class ClassHourChangedRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "school_id")
    private String schoolId;
    @Column(name = "activity_id")
    private String activity;
    @Column(name = "date")
    private Date date;
    @Column(name = "variation")
    private double variation;
}
