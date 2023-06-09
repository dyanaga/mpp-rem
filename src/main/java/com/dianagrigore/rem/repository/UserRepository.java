package com.dianagrigore.rem.repository;

import com.dianagrigore.rem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    Optional<User> findFirstByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE app_user u SET u.pagePreference = :pagePreference")
    void setPagePreferenceForAllUsers(int pagePreference);
}
