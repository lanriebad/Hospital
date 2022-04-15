package com.hospital.assessment.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class StaffResponse implements Serializable {

    private String name;

    private String uuid;

    private Date registration_date;
}
