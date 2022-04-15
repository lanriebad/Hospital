package com.hospital.assessment.repository;

import com.hospital.assessment.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
public interface PatientRepository extends JpaRepository<Patient, Serializable> {


    @Query(value ="select p from Patient p where p.age>='2'")
    List<Patient> findPatientByAgeGreaterThanEqualTwo();


    @Query(value = "from Patient t where last_visit_date BETWEEN :startDate AND :endDate")
    List<Patient> getAllBetweenDates(@Param("startDate") Date date, @Param("endDate") Date date2);

    @Transactional
    @Modifying
    @Query("DELETE FROM Patient where last_visit_date BETWEEN :startDate AND :endDate")
    void deleteRecordByDateRange(@Param("startDate") Date date, @Param("endDate") Date date2);
}
