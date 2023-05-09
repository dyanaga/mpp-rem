package com.dianagrigore.rem.repository;

import com.dianagrigore.rem.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

public interface OfferRepository extends JpaRepository<Offer, String>, JpaSpecificationExecutor<Offer> {
    @Modifying
    @Transactional
    void deleteByUserId(String userId);

    @Modifying
    @Transactional
    void deleteByListingId(String listingId);
}
