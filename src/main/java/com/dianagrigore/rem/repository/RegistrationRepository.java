package com.dianagrigore.rem.repository;

import com.dianagrigore.rem.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, String>, JpaSpecificationExecutor<Registration> {
    List<Registration> findAllByTimestampBefore(Date date);
}
