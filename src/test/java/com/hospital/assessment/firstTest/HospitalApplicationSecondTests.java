package com.hospital.assessment.firstTest;

import com.hospital.assessment.model.Staff;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
public class HospitalApplicationSecondTests {


    private static final Logger logger = LoggerFactory.getLogger(HospitalApplicationSecondTests.class);

    protected String externalUrl = "http://localhost:8086/api/";
    protected RestTemplate restTemplate = new RestTemplate();

    private Staff staffRequest;
    private String uuid="abcdef002233yyy";
    private String name = "Oluuuu";
    private Long id = Long.valueOf(888);
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
    public void createStaffTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();

        String url ="add/staff.json";
        URI uri = new URI(String.format("%s%s", externalUrl, url));
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> entity = restTemplate.postForEntity(uri, staffRequest,String.class);
        logger.info("entity {}", entity);
        logger.info("<<< RESPONSE >>>> {}", entity);
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(entity.getBody());



    }


}
