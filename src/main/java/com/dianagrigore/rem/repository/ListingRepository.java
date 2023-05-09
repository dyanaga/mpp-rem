package com.dianagrigore.rem.repository;

import com.dianagrigore.rem.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ListingRepository extends JpaRepository<Listing, String>, JpaSpecificationExecutor<Listing> {}
