package com.dianagrigore.rem.repository;

import com.dianagrigore.rem.model.User;
import com.dianagrigore.rem.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserLoginRepository extends JpaRepository<UserLogin, String>, JpaSpecificationExecutor<UserLogin> {
    List<UserLogin> findAllByTimestampBeforeAndIsActiveIsFalse(Date date);
    Optional<UserLogin> findUserLoginByUsername(String username);

    @Query(value = "SELECT u FROM user_login u WHERE username=:loginId AND password=:password AND is_active=true")
    List<UserLogin> login(@Param("loginId") String loginId, @Param("password") String password);

}
