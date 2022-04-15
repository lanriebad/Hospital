package com.hospital.assessment.utility;


import org.springframework.stereotype.Component;

import javax.persistence.PreUpdate;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


@Component
public class HospitalUtility {


    public final String generateUUID(){
        return UUID.randomUUID().toString();
    }



    public long generateNumberIdentifier(){
        synchronized (new Object()) {
            long randomInt = ThreadLocalRandom.current().nextInt(100, 999);
            return randomInt;        }
    }


    @PreUpdate
    public Date preUpdate(Date last_visit_date) {
        last_visit_date = new Date();
        return last_visit_date;
    }



}
