package com.hospital.assessment.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
public class Staff implements Serializable {

    @Id
    private Long id;

    private String name;

    @Column(unique=true)
    private String uuid;

    private Date registration_date;


//    @ManyToOne
//    private Hospital hospital;
}
