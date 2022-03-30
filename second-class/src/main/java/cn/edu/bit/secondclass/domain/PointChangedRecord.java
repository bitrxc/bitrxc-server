package cn.edu.bit.secondclass.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "point_changed_record")
public class PointChangedRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "school_id")
    private String schoolId;
    @Column(name = "admin_id")
    private int admin;
    @Column(name = "variation")
    private double variation;
    @Column(name = "date")
    private Date date;
    @Column(name = "reason")
    private String reason;
    @Column(name = "transaction_id")
    private Integer transaction;
}