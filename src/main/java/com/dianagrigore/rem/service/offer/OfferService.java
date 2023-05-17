package com.dianagrigore.rem.service.offer;

import com.dianagrigore.rem.dto.OfferDto;
import com.dianagrigore.rem.dto.pages.OfferPage;
import com.dianagrigore.rem.exception.ResourceNotFoundException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.validation.Valid;

/**
 * Service interface for offer related operations
 */
public interface OfferService {

    /**
     * Creates a new offer.
     *
     * @param offerToCreate - payload for creating the offer
     * @return - new created offer
     */
    OfferDto createOffer(@NonNull String listingId, @Valid OfferDto offerToCreate);

    OfferDto updateOffer(String userId, @NonNull String offerId, @Valid OfferDto offerToCreate);

    /**
     * Search offer for listing using a filter and pagination parameters
     *
     * @param listingId - id of the listing
     * @param filter    - basic FIQL filter, might be null
     * @param page      - desired page, might be null and default will be 0.
     * @param pageSize  - desired page size, might be null and default will be 100.
     * @param sort      - direction of the sort, might be null, but default will be name ascending.
     * @param expand    - the subfields that needs to be expanded (i.e. "account" or "account,card")
     * @return - paged offers by filter and having desired pagination.
     */
    OfferPage findOffers(@Nullable String filter, @Nullable Integer page, @Nullable Integer pageSize, @Nullable String sort,
            @Nullable String expand);

    /**
     * Search offer for listing using a filter and pagination parameters
     *
     * @param userId   - Id of the user
     * @param filter   - basic FIQL filter, might be null
     * @param page     - desired page, might be null and default will be 0.
     * @param pageSize - desired page size, might be null and default will be 100.
     * @param sort     - direction of the sort, might be null, but default will be name ascending.
     * @param expand   - the subfields that needs to be expanded (i.e. "account" or "account,card")
     * @return - paged offers by filter and having desired pagination.
     */
    OfferPage findOffersForUser(@NonNull String userId, @Nullable String filter, @Nullable Integer page, @Nullable Integer pageSize, @Nullable String sort,
            @Nullable String expand);

    /**
     * Searches for a specific offer by id
     *
     * @param offerId - the id of the offer which is searched, cannot be null
     * @return - the desired offer
     * @throws ResourceNotFoundException if the offer was not found.
     */
    OfferDto getOffer(@NonNull String offerId);

    /**
     * Delete an offer based on the id.
     *
     * @param offerId - the id of the offer which is searched, cannot be null
     * @return - the updated offer
     * @throws ResourceNotFoundException if the offer was not found.
     */
    OfferDto deleteOffer(@NonNull String offerId);

}
