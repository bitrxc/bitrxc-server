package cn.edu.bit.secondclass.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "point_exchanged_record")
public class PointExchangedRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "reason")
    private String reason;
    @Column(name ="school_id")
    private String schoolId;
    @Column(name = "admin_id")
    private Integer admin;
    @Column(name = "product_id")
    private int product;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "status")
    private String status;
    @Column(name = "time")
    private Date time;
    @Column(name = "checkNote")
    private String checkNote;
}
