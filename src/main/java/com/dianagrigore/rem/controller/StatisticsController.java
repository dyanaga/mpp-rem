package com.dianagrigore.rem.controller;

import com.dianagrigore.rem.model.enums.UserType;
import com.dianagrigore.rem.model.statistics.ListingsPerNeighbourhood;
import com.dianagrigore.rem.permissions.PermissionCheck;
import com.dianagrigore.rem.service.statistics.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping(value = "/listings-per-neighbourhood")
    @PermissionCheck(hasAny = {UserType.DIRECTOR, UserType.ADMIN})
    public List<ListingsPerNeighbourhood> getListingsPerNeighbourhood() {
        return statisticsService.getListingsPerNeighbourhood();
    }

    @GetMapping(value = "/listings-by-offers")
    @PermissionCheck(hasAny = {UserType.DIRECTOR, UserType.ADMIN})
    public List<Map<String, Object>> getListingsByOfferCount() {
        return statisticsService.getListingsByOfferCount();
    }

}
