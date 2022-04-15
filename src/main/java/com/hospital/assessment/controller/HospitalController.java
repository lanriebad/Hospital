package com.hospital.assessment.controller;


import com.hospital.assessment.dto.HospitalRequest;
import com.hospital.assessment.dto.PatientRequest;
import com.hospital.assessment.dto.StaffRequest;
import com.hospital.assessment.model.Staff;
import com.hospital.assessment.repository.StaffRepository;
import com.hospital.assessment.service.HospitalService;
import com.hospital.assessment.utility.DefaultServiceResponse;
import com.hospital.assessment.utility.HospitalUtility;
import com.hospital.assessment.utility.ServiceResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HospitalController {


    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private StaffRepository staffRepository;

    @Value("${age.range.for.two:2}")
    public String ageRangeForTwo;


    @Value("${no.patient.record:No Record Found}")
    public String noRecord;

    @Autowired
   HospitalUtility hospitalUtility;



    @PostMapping(value = "add/staff.json")
    public DefaultServiceResponse createStaff(@RequestBody StaffRequest request) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        final long requestId = hospitalUtility.generateNumberIdentifier();
        request.setRequestId(requestId);
        request.setUuid(hospitalUtility.generateUUID());
        DefaultServiceResponse createStaff = hospitalService.addStaff(request);
        if (!createStaff.getResponseData().isEmpty()) {
            response.setResponseCode(ServiceResponse.ResponseCode.SUCCESS.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.SUCCESS.getDefaultMessage());
            response.setResponseData(createStaff.getResponseData());
        } else {
            response.setResponseCode(ServiceResponse.ResponseCode.ERROR.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.ERROR.getDefaultMessage());
        }

        return response;
    }


    @PutMapping(value = "update/staff.json")
    public DefaultServiceResponse updateStaff(@RequestBody StaffRequest request) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        Optional<Staff> optionalStaff = staffRepository.findByUuid(request.getUuid());
        if (optionalStaff.isPresent()) {
            Staff staff = optionalStaff.get();
            DefaultServiceResponse updateResponse= hospitalService.updateStaff(request, staff);
            response.setResponseCode(ServiceResponse.ResponseCode.SUCCESS.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.SUCCESS.getDefaultMessage());
            response.setResponseData(updateResponse.getResponseData());
        } else {
            response.setResponseCode(ServiceResponse.ResponseCode.USER_NOT_FOUND.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.USER_NOT_FOUND.getDefaultMessage());
        }
        return response;
    }


    @PostMapping(value = "add/patient.json")
    public DefaultServiceResponse createPatient(@RequestBody PatientRequest request) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        response = hospitalService.addPatient(request);
        return response;
    }


    @GetMapping(value = "/get/patients.json")
    public DefaultServiceResponse getPatientUpToTwoYears(@RequestParam String uuid) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        Optional<Staff> optionalStaff = staffRepository.findByUuid(uuid);
        if (optionalStaff.isPresent()) {
            List result = hospitalService.getPatientUpToTwoYears();
            if (!result.isEmpty()) {
                response.setResponseData(result);
                response.setResponseCode(ServiceResponse.ResponseCode.SUCCESS.getCode());
                response.setResponseMsg(ServiceResponse.ResponseCode.SUCCESS.getDefaultMessage());

            } else {
                response.setResponseCode(ServiceResponse.ResponseCode.INVALID_AGE_RANGE.getCode());
                response.setResponseMsg(String.format(ServiceResponse.ResponseCode.INVALID_AGE_RANGE.getDefaultMessage(), ageRangeForTwo));
            }
        } else {
            response.setResponseCode(ServiceResponse.ResponseCode.USER_NOT_FOUND.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.USER_NOT_FOUND.getDefaultMessage());
        }
        return response;
    }


    @DeleteMapping(value = "delete/profile.json")
    public DefaultServiceResponse deleteProfileBetweenDateRange(@RequestBody StaffRequest request) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        Optional<Staff> optionalStaff = staffRepository.findByUuid(request.getUuid());
        if (optionalStaff.isPresent()) {
            response =hospitalService.deleteProfileBetweenDateRange(request);
        } else {
            response.setResponseCode(ServiceResponse.ResponseCode.USER_NOT_FOUND.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.USER_NOT_FOUND.getDefaultMessage());
        }
        return response;
    }


    @RequestMapping(value = {"patient/csv.json"}, method = RequestMethod.POST)
    public DefaultServiceResponse downloadPatientCsv(@RequestBody HospitalRequest request, HttpServletResponse response) throws IOException {
        DefaultServiceResponse defaultResponse = new DefaultServiceResponse();
        String fileName = StringUtils.EMPTY;
        Map<String, Object> mp = new HashMap<>();
        String message = StringUtils.EMPTY;
        fileName = hospitalService.downloadPatientCsv(request, response, fileName);
        if(StringUtils.isNotEmpty(fileName)) {
            message = String.format("Kindly find the CSV file from this path %s", fileName);
            mp.put("Message", message);
            defaultResponse.setResponseCode(ServiceResponse.ResponseCode.SUCCESS.getCode());
            defaultResponse.setResponseMsg(ServiceResponse.ResponseCode.SUCCESS.getDefaultMessage());
            defaultResponse.setResponseData(Collections.singletonList(mp));
        }else{
            defaultResponse.setResponseCode(ServiceResponse.ResponseCode.USER_NOT_FOUND.getCode());
            defaultResponse.setResponseMsg(ServiceResponse.ResponseCode.USER_NOT_FOUND.getDefaultMessage());
            defaultResponse.setResponseData(Collections.singletonList(noRecord));
        }

        return defaultResponse;
    }


}
