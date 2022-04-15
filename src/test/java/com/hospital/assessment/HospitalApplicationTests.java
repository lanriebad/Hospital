package com.hospital.assessment;

import com.hospital.assessment.dto.StaffRequest;
import com.hospital.assessment.firstTest.HospitalApplicationSecondTests;
import com.hospital.assessment.model.Staff;
import com.hospital.assessment.repository.StaffRepository;
import com.hospital.assessment.service.HospitalServiceImpl;
import com.hospital.assessment.utility.DefaultServiceResponse;
import com.hospital.assessment.utility.ServiceResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
public class HospitalApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(HospitalApplicationTests.class);


 @Autowired
    StaffRepository staffRepository;

    private Staff staffRequest;
    private String uuid="abcdef002233";
    private String name = "Oluuui";
    private Long id = Long.valueOf(928);
    private Date regDate=new Date();




    @Before
    public void setUp() {
        staffRequest = new Staff();
        staffRequest.setName(name);
        staffRequest.setUuid(uuid);
        staffRequest.setId(id);
        staffRequest.setRegistration_date(regDate);

        MockitoAnnotations.initMocks(this);
    }



        @Test
    public void createStaffTest() {
        staffRepository.save(staffRequest);
            Optional<Staff> byUuid = staffRepository.findByUuid(uuid);
            logger.info("<<< RESPONSE >>>> {}", byUuid.get());
            Assert.assertNotNull(byUuid);
    }





}
