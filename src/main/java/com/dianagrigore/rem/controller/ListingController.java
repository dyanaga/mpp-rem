package com.dianagrigore.rem.controller;

import com.dianagrigore.rem.api.ListingApi;
import com.dianagrigore.rem.dto.ListingDto;
import com.dianagrigore.rem.dto.pages.ListingPage;
import com.dianagrigore.rem.model.enums.UserType;
import com.dianagrigore.rem.permissions.PermissionCheck;
import com.dianagrigore.rem.service.listing.ListingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/v1")
public class ListingController implements ListingApi {

    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @Override
    @PermissionCheck(hasAny = {UserType.AGENT, UserType.DIRECTOR, UserType.ADMIN})
    public ListingDto createListing(@Valid ListingDto listing) {
        return listingService.createListing(listing);
    }

    @Override
    public ListingPage findListings(@Valid String filter, @Min(0) @Valid Integer page, @Min(1) @Max(200) @Valid Integer pageSize,
            @Pattern(regexp = "[+-]\\w+(,[+-]\\w+)*") @Valid String sort, @Pattern(regexp = "\\w+(,\\w+)*") @Valid String expand) {
        return listingService.findListings(filter, page, pageSize, sort, expand);
    }

    @Override
    public ListingDto getListing(String listingId) {
        return listingService.getListing(listingId);
    }

    @Override
    @PermissionCheck(hasAny = {UserType.AGENT, UserType.DIRECTOR, UserType.ADMIN})
    public ListingDto updateListing(String listingId, @Valid ListingDto listingWithUpdates) {
        return listingService.updateListing(listingId, listingWithUpdates);
    }

    @Override
    @PermissionCheck(hasAny = {UserType.AGENT, UserType.DIRECTOR, UserType.ADMIN})
    public ListingDto deleteListing(String listingId) {
        return listingService.deleteListing(listingId);
    }

    @Override
    @PermissionCheck(hasAny = {UserType.AGENT, UserType.DIRECTOR, UserType.ADMIN})
    public ListingDto enrollListing(String listingId) {
        return listingService.enrollListing(listingId);
    }

    @Override
    @PermissionCheck(hasAny = {UserType.DIRECTOR, UserType.ADMIN})
    public ListingDto enrollListingSpecific(String listingId, String userId) {
        return listingService.enrollListingSpecific(listingId, userId);
    }

    @Override
    @PermissionCheck(hasAny = {UserType.AGENT, UserType.DIRECTOR, UserType.ADMIN})
    public ListingDto quitListing(String listingId) {
        return listingService.quitListing(listingId);
    }

    @Override
    @PermissionCheck(hasAny = {UserType.DIRECTOR, UserType.ADMIN})
    public ListingDto quitListingSpecific(String listingId, String userId) {
        return listingService.quitListingSpecific(listingId, userId);
    }
}
