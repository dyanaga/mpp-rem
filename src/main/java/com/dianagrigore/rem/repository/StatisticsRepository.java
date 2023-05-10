package com.dianagrigore.rem.repository;

import com.dianagrigore.rem.model.AgentListing;
import com.dianagrigore.rem.model.AgentListingKey;
import com.dianagrigore.rem.model.Listing;
import com.dianagrigore.rem.model.statistics.ListingsByOfferCount;
import com.dianagrigore.rem.model.statistics.ListingsPerNeighbourhood;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<Listing, String> {

    @Query("select new com.dianagrigore.rem.model.statistics.ListingsPerNeighbourhood(neighbourhood, COUNT(neighbourhood)) from listing group by neighbourhood")
    List<ListingsPerNeighbourhood> getListingsPerNeighbourhood();

    @Query("select new com.dianagrigore.rem.model.statistics.ListingsByOfferCount(count(listingId), listingId) from offer group by listingId order by count(listingId) desc ")
    List<ListingsByOfferCount> getTopListingsByOfferCount(Pageable pageable);

}
