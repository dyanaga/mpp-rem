package com.dianagrigore.rem.service.listing;

import com.dianagrigore.rem.dto.ListingDto;
import com.dianagrigore.rem.dto.pages.ListingPage;
import com.dianagrigore.rem.exception.ResourceNotFoundException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.validation.Valid;

/**
 * Service interface for listing related operations
 */
public interface ListingService {

    /**
     * Creates a new listing.
     *
     * @param listingToCreate - payload for creating the listing
     * @return - new created listing
     */
    ListingDto createListing(@Valid ListingDto listingToCreate);

    /**
     * Search listing using a filter and pagination parameters
     *
     * @param filter   - basic FIQL filter, might be null
     * @param page     - desired page, might be null and default will be 0.
     * @param pageSize - desired page size, might be null and default will be 100.
     * @param sort     - direction of the sort, might be null, but default will be name ascending.
     * @param expand   - the subfields that needs to be expanded (i.e. "account" or "account,card")
     * @return - paged listings by filter and having desired pagination.
     */
    ListingPage findListings(@Nullable String filter, @Nullable Integer page, @Nullable Integer pageSize, @Nullable String sort, @Nullable String expand);

    /**
     * Searches for a specific listing by id
     *
     * @param listingId - the id of the listing which is searched, cannot be null
     * @return - the desired listing
     * @throws ResourceNotFoundException if the listing was not found.
     */
    ListingDto getListing(@NonNull String listingId);

    /**
     * Updates a listing with the given updates (password field is ignored).
     * or cards will be modified as well
     *
     * @param listingId          - the id of the listing which is wanted to be updated, cannot be null.
     * @param listingWithUpdates - the listing with the updated fields.
     * @return - the updated listing.
     * @throws ResourceNotFoundException if the listing was not found.
     */
    ListingDto updateListing(@NonNull String listingId, @Valid ListingDto listingWithUpdates);

    /**
     * Delete a listing based on the id.
     *
     * @param listingId - the id of the listing which is searched, cannot be null
     * @return - the updated listing
     * @throws ResourceNotFoundException if the listing was not found.
     */
    ListingDto deleteListing(@NonNull String listingId);

    ListingDto enrollListing(String listingId);

    ListingDto enrollListingSpecific(String listingId, String userId);

    ListingDto quitListing(String listingId);

    ListingDto quitListingSpecific(String listingId, String userId);
}
