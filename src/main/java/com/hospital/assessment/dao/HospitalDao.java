package com.hospital.assessment.dao;

import com.hospital.assessment.dto.StaffRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class HospitalDao {


    private static final Log LOG = LogFactory.getLog(HospitalDao.class);


    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    JdbcTemplate jdbcTemplate;


    public String deleteRecordByDateRange(StaffRequest request) {
            MapSqlParameterSource param = new MapSqlParameterSource();
            param.addValue("startDate", getEffectiveStartDate(request.getStartDate()), Types.TIMESTAMP);
            param.addValue("endDate", getEffectiveEndDate(request.getEndDate()), Types.TIMESTAMP);
            String sql = "DELETE FROM Patient where last_visit_date between :startDate and :endDate ";
            LOG.info("jpql: " + sql);
            String result = namedParameterJdbcTemplate.queryForObject(sql, param, String.class);
        return result;
    }


    private Date getEffectiveStartDate(Date startDate) {
        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Date.from(startLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date getEffectiveEndDate(Date endDate) {
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Date.from(endLocalDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
    }


    public List<Map<String, Object>> getPatientRecords(Long id) {
        List<Map<String, Object>> result = new ArrayList<>();
        try{
            String sql= String.format("select * from patient where id= %d ",id);
           result=jdbcTemplate.queryForList(sql);
        }catch(Exception e){
            e.printStackTrace();
        }

  return result;
    }
}
