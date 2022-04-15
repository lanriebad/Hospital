package com.hospital.assessment.repository;

import com.hospital.assessment.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;


@Repository
public interface StaffRepository extends JpaRepository<Staff, Serializable> {

    Optional<Staff> findByUuid(String uuid);
}
