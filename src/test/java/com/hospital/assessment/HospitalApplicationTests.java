package com.hospital.assessment;

import com.hospital.assessment.dto.StaffRequest;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
public class HospitalApplicationTests {



 @Autowired
    StaffRepository staffRepository;

    private Staff staffRequest;
    private String uuid="abcdef00223355";
    private String name = "Oluuii";
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
        Assert.assertNotNull(staffRepository.findByUuid(uuid));
    }





}
