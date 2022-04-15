package com.hospital.assessment.service;

import com.hospital.assessment.dto.HospitalRequest;
import com.hospital.assessment.dto.PatientRequest;
import com.hospital.assessment.dto.StaffRequest;
import com.hospital.assessment.model.Staff;
import com.hospital.assessment.utility.DefaultServiceResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface HospitalService {
    DefaultServiceResponse addStaff(StaffRequest request);

    DefaultServiceResponse updateStaff(StaffRequest request, Staff staff);

    DefaultServiceResponse addPatient(PatientRequest request);

    List getPatientUpToTwoYears();

    DefaultServiceResponse deleteProfileBetweenDateRange(StaffRequest request);

    String downloadPatientCsv(HospitalRequest request, HttpServletResponse response,String fileName) throws IOException;
}
