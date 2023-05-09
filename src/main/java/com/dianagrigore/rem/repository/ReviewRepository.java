package com.dianagrigore.rem.repository;

import com.dianagrigore.rem.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

public interface ReviewRepository extends JpaRepository<Review, String>, JpaSpecificationExecutor<Review> {

    @Modifying
    @Transactional
    void deleteByUserId(String userId);
}
