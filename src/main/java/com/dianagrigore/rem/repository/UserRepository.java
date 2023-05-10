package com.dianagrigore.rem.repository;

import com.dianagrigore.rem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    @Query(value = "SELECT u FROM app_user u WHERE email=:loginId AND password=:password AND is_active=true")
    List<User> login(@Param("loginId") String loginId, @Param("password") String password);

    List<User> findAllByEmail(String email);
}
