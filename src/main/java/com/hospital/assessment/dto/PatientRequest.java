package com.hospital.assessment.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class PatientRequest implements Serializable {


    private String name;

    private String uuid;

    private Date last_visit_date;

    private String age;


}
