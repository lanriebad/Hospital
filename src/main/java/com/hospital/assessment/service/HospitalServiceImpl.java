package com.hospital.assessment.service;


import com.hospital.assessment.dao.HospitalDao;
import com.hospital.assessment.dto.HospitalRequest;
import com.hospital.assessment.dto.PatientRequest;
import com.hospital.assessment.dto.StaffRequest;
import com.hospital.assessment.model.Patient;
import com.hospital.assessment.model.Staff;
import com.hospital.assessment.repository.PatientRepository;
import com.hospital.assessment.repository.StaffRepository;
import com.hospital.assessment.utility.DefaultServiceResponse;
import com.hospital.assessment.utility.HospitalUtility;
import com.hospital.assessment.utility.ServiceResponse;
import com.opencsv.CSVWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service("hospitalService")
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private HospitalUtility hospitalUtility;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private HospitalDao hospitalDao;


    @Override
    public DefaultServiceResponse addStaff(StaffRequest request) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        Staff staff = new Staff();
        staff.setName(request.getName());
        staff.setUuid(request.getUuid());
        staff.setRegistration_date(new Date());
        staff.setId(request.getRequestId());
        staffRepository.save(staff);
        Optional<Staff> optionalRecord = staffRepository.findByUuid(staff.getUuid());
        if (optionalRecord.isPresent()) {
            Staff staffUUid = optionalRecord.get();
            response.setResponseData(Collections.singletonList(staffUUid));
        }
        return response;

    }

    @Override
    public DefaultServiceResponse updateStaff(StaffRequest request, Staff updateStaff) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        updateStaff.setName(request.getName());
        updateStaff.setUuid(request.getUuid());
        staffRepository.save(updateStaff);
        Optional<Staff> optionalRecord = staffRepository.findByUuid(request.getUuid());
        if (optionalRecord.isPresent()) {
            Staff staffRecord = optionalRecord.get();
            response.setResponseData(Collections.singletonList(staffRecord));
        }

        return response;
    }

    @Override
    public DefaultServiceResponse addPatient(PatientRequest request) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        final long requestId = hospitalUtility.generateNumberIdentifier();
        Patient patient = new Patient();
        patient.setAge(request.getAge());
        patient.setName(request.getName());
        patient.setId(requestId);
        patient.setLast_visit_date(new Date());
        patientRepository.save(patient);
        Optional<Patient> optionalPatient = patientRepository.findById(requestId);
        if (optionalPatient.isPresent()) {
            Patient records = optionalPatient.get();
            response.setResponseCode(ServiceResponse.ResponseCode.SUCCESS.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.SUCCESS.getDefaultMessage());
            response.setResponseData(Collections.singletonList(records));

        } else {
            response.setResponseCode(ServiceResponse.ResponseCode.ERROR.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.ERROR.getDefaultMessage());
        }

        return response;
    }

    @Override
    public List getPatientUpToTwoYears() {
        List<Patient> patientByAgeGreaterThanEqualTwo = patientRepository.findPatientByAgeGreaterThanEqualTwo();
        patientByAgeGreaterThanEqualTwo.stream().map(result -> {
            Patient patient = new Patient();
            patient.setName(result.getName());
            patient.setAge(result.getAge());
            patient.setLast_visit_date(hospitalUtility.preUpdate(result.getLast_visit_date()));
            return patient;
        }).collect(Collectors.toList());
        return patientByAgeGreaterThanEqualTwo;

    }

    @Override
    public DefaultServiceResponse deleteProfileBetweenDateRange(StaffRequest request) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        Date startDate = getEffectiveStartDate(request.getStartDate());
        Date endDate = getEffectiveEndDate(request.getEndDate());
        String message = StringUtils.EMPTY;
        Map<String, Object> mp = new HashMap<>();
        List<Patient> optionalDate = patientRepository.getAllBetweenDates(startDate, endDate);
        if (!optionalDate.isEmpty()) {
            patientRepository.deleteRecordByDateRange(startDate, endDate);
            response.setResponseCode(ServiceResponse.ResponseCode.SUCCESS.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.SUCCESS.getDefaultMessage());
            message = String.format("Date Range From %s  to  %s  has been deleted.", startDate, endDate);
            mp.put("Message", message);
            response.setResponseData(Collections.singletonList(mp));
        } else {
            message = String.format("Date Range From %s  to  %s  does not exist.", startDate, endDate);
            mp.put("Message", message);
            response.setResponseData(Collections.singletonList(mp));
            response.setResponseCode(ServiceResponse.ResponseCode.BAD_REQUEST.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.BAD_REQUEST.getDefaultMessage());
        }
        return response;

    }


    @Override
    public String downloadPatientCsv(HospitalRequest request, HttpServletResponse response, String fileName) throws IOException {
        FileWriter fileWriter = null;
        CSVWriter writer = null;
        Optional<Staff> optionalStaff = staffRepository.findByUuid(request.getStaffUuid());
        if (optionalStaff.isPresent()) {
            List<Map<String, Object>> results = hospitalDao.getPatientRecords(request.getPatientId());
            if (!results.isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    fileName = System.getProperty("user.dir") + "patient-" + hospitalUtility.generateNumberIdentifier() + ".csv";
                    fileWriter = new FileWriter(fileName);
                    writer = new CSVWriter(fileWriter);
                    HashSet<String> headersList = new LinkedHashSet<>();
                    for (String key : results.get(0).keySet()) {
                        headersList.add(key);
                    }
                    String[] headerArray = new String[headersList.size()];
                    headersList.toArray(headerArray);
                    writer.writeNext(headerArray);
                    for (int i = 0; i < results.size(); i++) {
                        String id = String.valueOf(results.get(i).get("id"));
                        String name = (String) results.get(i).get("name");
                        String age = (String) results.get(i).get("age");
                        String last_visit_date = sdf.format(results.get(i).get("last_visit_date"));
                        writer.writeNext(
                                new String[]{id, name, age, last_visit_date});
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    writer.flush();
                    fileWriter.close();
                }
            }
        }

        return fileName;
    }


    private Date getEffectiveStartDate(Date startDate) {
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Date.from(startLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date getEffectiveEndDate(Date endDate) {
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Date.from(endLocalDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
    }

}
