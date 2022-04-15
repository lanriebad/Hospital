package com.hospital.assessment.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class Patient implements Serializable {

    @Id
    private Long id;

    private String name;

    private String age;

    private Date last_visit_date;

    @PreUpdate
    public void preUpdate() {
        last_visit_date = new Date();
    }




}
