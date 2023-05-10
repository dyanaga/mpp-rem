package com.dianagrigore.rem.service.statistics;

import com.dianagrigore.rem.dto.ListingDto;
import com.dianagrigore.rem.exception.BaseException;
import com.dianagrigore.rem.model.Listing;
import com.dianagrigore.rem.model.statistics.ListingsByOfferCount;
import com.dianagrigore.rem.model.statistics.ListingsPerNeighbourhood;
import com.dianagrigore.rem.repository.ListingRepository;
import com.dianagrigore.rem.repository.StatisticsRepository;
import com.dianagrigore.rem.web.converter.BasicMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final ListingRepository listingRepository;
    private final BasicMapper<Listing, ListingDto> listingDtoBasicMapper;

    public StatisticsService(StatisticsRepository statisticsRepository, ListingRepository listingRepository, BasicMapper<Listing, ListingDto> listingDtoBasicMapper) {
        this.statisticsRepository = statisticsRepository;
        this.listingRepository = listingRepository;
        this.listingDtoBasicMapper = listingDtoBasicMapper;
    }

    public List<ListingsPerNeighbourhood> getListingsPerNeighbourhood() {
        return statisticsRepository.getListingsPerNeighbourhood();
    }

    public List<Map<String, Object>> getListingsByOfferCount() {

        List<ListingsByOfferCount> topListingsByOfferCount = statisticsRepository.getTopListingsByOfferCount(PageRequest.of(0, 20));
        List<Map<String, Object>> result = new ArrayList<>();
        topListingsByOfferCount.forEach(listingsByOfferCount -> {
            Listing listing = listingRepository.findById(listingsByOfferCount.getListingId()).orElseThrow(() -> new BaseException("Impossible."));
            Map<String, Object> map = new HashMap<>();
            map.put("count", listingsByOfferCount.getCount());
            map.put("listingId", listing.getListingId());
            map.put("name", listing.getName());
            map.put("address", listing.getAddress());
            result.add(map);
        });
        return result;
    }

}
