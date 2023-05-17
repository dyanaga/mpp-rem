package com.dianagrigore.rem.controller;

import com.dianagrigore.rem.api.OfferApi;
import com.dianagrigore.rem.dto.OfferDto;
import com.dianagrigore.rem.dto.pages.OfferPage;
import com.dianagrigore.rem.model.enums.UserType;
import com.dianagrigore.rem.permissions.PermissionCheck;
import com.dianagrigore.rem.service.offer.OfferService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class OfferController implements OfferApi {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @Override
    @PermissionCheck(hasAny = {UserType.CLIENT, UserType.AGENT, UserType.DIRECTOR, UserType.ADMIN})
    public OfferDto createOffer(String listingId, OfferDto offer) {
        return offerService.createOffer(listingId, offer);
    }

    @Override
    @PermissionCheck(hasAny = {UserType.CLIENT, UserType.AGENT, UserType.DIRECTOR, UserType.ADMIN})
    public OfferDto updateOffer(String userId, String offerId, OfferDto offer) {
        return offerService.updateOffer(userId, offerId, offer);
    }

    @Override
    @PermissionCheck(hasAny = {UserType.CLIENT, UserType.AGENT, UserType.DIRECTOR, UserType.ADMIN})
    public OfferPage findOffers(String filter, Integer page, Integer pageSize, String sort, String expand) {
        return offerService.findOffers(filter, page, pageSize, sort, expand);
    }

    @Override
    @PermissionCheck(hasAny = {UserType.CLIENT, UserType.AGENT, UserType.DIRECTOR, UserType.ADMIN})
    public OfferPage findOffersForUser(String userId, String filter, Integer page, Integer pageSize, String sort, String expand) {
        return offerService.findOffersForUser(userId, filter, page, pageSize, sort, expand);
    }

    @Override
    @PermissionCheck(hasAny = {UserType.CLIENT, UserType.AGENT, UserType.DIRECTOR, UserType.ADMIN})
    public OfferDto getOffer(String offerId) {
        return offerService.getOffer(offerId);
    }

    @Override
    @PermissionCheck(hasAny = {UserType.CLIENT, UserType.AGENT, UserType.DIRECTOR, UserType.ADMIN})
    public OfferDto deleteOffer(String offerId, String userId) {
        return offerService.deleteOffer(offerId);
    }
}
