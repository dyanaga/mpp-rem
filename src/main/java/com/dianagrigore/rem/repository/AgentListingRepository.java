package com.dianagrigore.rem.repository;

import com.dianagrigore.rem.model.AgentListing;
import com.dianagrigore.rem.model.AgentListingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface AgentListingRepository extends JpaRepository<AgentListing, AgentListingKey>, JpaSpecificationExecutor<AgentListing> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM AgentListing a WHERE a.listingId=:listingId")
    void deleteByListingId(String listingId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM AgentListing a WHERE a.userId=:userId")
    void deleteByAgentId(String userId);

    List<AgentListing> findAllByUserId(String userId);

}
