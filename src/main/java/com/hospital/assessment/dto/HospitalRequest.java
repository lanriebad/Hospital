package com.hospital.assessment.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class HospitalRequest implements Serializable {

    private String staffUuid;

    private Long patientId;


}
