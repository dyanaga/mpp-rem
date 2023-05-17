package com.dianagrigore.rem.service.offer.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.dianagrigore.rem.dto.OfferDto;
import com.dianagrigore.rem.dto.pages.OfferPage;
import com.dianagrigore.rem.dto.token.AuthenticationType;
import com.dianagrigore.rem.exception.BaseException;
import com.dianagrigore.rem.exception.PermissionDeniedException;
import com.dianagrigore.rem.exception.ResourceNotFoundException;
import com.dianagrigore.rem.model.Listing;
import com.dianagrigore.rem.model.Offer;
import com.dianagrigore.rem.model.User;
import com.dianagrigore.rem.repository.ListingRepository;
import com.dianagrigore.rem.repository.OfferRepository;
import com.dianagrigore.rem.repository.UserRepository;
import com.dianagrigore.rem.security.SecurityService;
import com.dianagrigore.rem.service.offer.OfferService;
import com.dianagrigore.rem.utils.expand.ExpandBuilder;
import com.dianagrigore.rem.utils.expand.ExpandableFields;
import com.dianagrigore.rem.web.converter.BasicMapper;
import com.dianagrigore.rem.web.filter.FieldFilter;
import com.dianagrigore.rem.web.filter.FilterUtils;
import com.dianagrigore.rem.web.filter.specifications.StringSpecifications;
import com.dianagrigore.rem.web.paging.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class OfferServiceImpl implements OfferService {
    private static final Logger logger = LoggerFactory.getLogger(OfferServiceImpl.class);

    private static final List<String> FIND_ALLOWED_STRING_FILTERS = List.of("comment");
    private final OfferRepository offerRepository;
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;
    private final BasicMapper<Offer, OfferDto> basicMapper;
    private final SecurityService securityService;

    public OfferServiceImpl(OfferRepository offerRepository, UserRepository userRepository, ListingRepository listingRepository, BasicMapper<Offer, OfferDto> basicMapper,
            SecurityService securityService) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
        this.basicMapper = basicMapper;
        this.securityService = securityService;
    }

    @Override
    public OfferDto createOffer(@NonNull String listingId, @Valid OfferDto offerToCreate) {
        logger.debug("Started to create offer.");
        Offer offer = basicMapper.convertTarget(offerToCreate);
        String agentId = securityService.getUserId();
        User user = getUserOrThrow(agentId);
        Listing listing = getListingOrThrow(listingId);
        offer.setUser(user);
        offer.setUserId(user.getUserId());
        offer.setListing(listing);
        offer.setListingId(listing.getListingId());

        Offer savedOffer = offerRepository.save(offer);
        logger.debug("Offer with id {} successfully created.", savedOffer.getOfferId());

        return basicMapper.convertSource(savedOffer, ExpandBuilder.of(ExpandableFields.USER).and(ExpandableFields.LISTING).toString());
    }

    @Override
    public OfferDto updateOffer(String userId, String offerId, OfferDto offerToCreate) {
        Offer offer = getOfferOrThrow(offerId);
        AuthenticationType authenticationType = securityService.getAuthenticationType();
        if(nonNull(userId) && !Objects.equals(offer.getUserId(), userId)) {
            throw new BaseException("Cannot update for different user!");
        }
        if(isNull(userId) && !(AuthenticationType.ADMIN.equals(authenticationType) || AuthenticationType.DIRECTOR.equals(authenticationType)))  {
            throw new PermissionDeniedException("Cannot access this resource.");
        }
        offer.setPrice(offerToCreate.getPrice());
        offer.setComment(offerToCreate.getComment());
        offer.setTimestamp(new Date());
        Offer savedOffer = offerRepository.save(offer);
        return basicMapper.convertSource(savedOffer, ExpandBuilder.of(ExpandableFields.USER).and(ExpandableFields.LISTING).toString());
    }

    @Override
    public OfferPage findOffers(@Nullable String filter, @Nullable Integer page, @Nullable Integer pageSize, @Nullable String sort,
            @Nullable String expand) {
        return findOffers(null, filter, page, pageSize, sort, expand);
    }

    @Override
    public OfferPage findOffersForUser(@NonNull String userId, @Nullable String filter, @Nullable Integer page, @Nullable Integer pageSize, @Nullable String sort,
            @Nullable String expand) {
        return findOffers(StringSpecifications.fieldEquals("userId", userId), filter, page, pageSize, sort, expand);
    }

    private OfferPage findOffers(Specification<Offer> indexSpecification, @Nullable String filter, @Nullable Integer page, @Nullable Integer pageSize,
            @Nullable String sort, @Nullable String expand) {
        logger.debug("Find offers called with parameters: filter=[{}], page=[{}], pageSize=[{}], sort=[{}], expand=[{}]", filter, page, pageSize, sort, expand);
        Pageable pageable = PageUtils.getPageable(page, pageSize, sort, "-price");
        Map<String, FieldFilter> fieldFiltersMap = FilterUtils.getFieldFiltersMap(filter);

        List<Specification<Offer>> specifications = new ArrayList<>();
        Optional.ofNullable(indexSpecification).ifPresent(specifications::add);
        StringSpecifications.appendSpecifications(specifications, fieldFiltersMap, FIND_ALLOWED_STRING_FILTERS);

        Specification<Offer> specification = specifications.stream().reduce(Specification::and).orElse(null);

        Page<Offer> resultPage = offerRepository.findAll(specification, pageable);
        logger.debug("Page of size=[{}] successfully got from the repository.", resultPage.getNumberOfElements());
        return new OfferPage(resultPage, basicMapper.convertSource(resultPage.getContent(), expand));
    }

    @Override
    public OfferDto getOffer(@NonNull String offerId) {
        logger.debug("Get offer by id=[{}] requested.", offerId);
        Offer offer = getOfferOrThrow(offerId);
        logger.debug("Offer with id=[{}] successfully found.", offerId);
        return basicMapper.convertSource(offer, ExpandBuilder.of(ExpandableFields.USER).and(ExpandableFields.LISTING).toString());
    }

    @Override
    public OfferDto deleteOffer(@NonNull String offerId) {
        logger.debug("Delete offer by id=[{}] requested.", offerId);
        Offer offer = getOfferOrThrow(offerId);

        offerRepository.delete(offer);

        logger.debug("Successfully deleted offer id=[{}].", offerId);
        return basicMapper.convertSource(offer);
    }

    private Offer getOfferOrThrow(@NonNull String offerId) {
        Optional<Offer> maybeOffer = offerRepository.findById(offerId);
        if (maybeOffer.isEmpty()) {
            throw new ResourceNotFoundException("Offer with this offerId does not exists!");
        }
        return maybeOffer.get();
    }

    private User getUserOrThrow(@NonNull String userId) {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new ResourceNotFoundException("User with given userId does not exists!");
        }
        return maybeUser.get();
    }

    private Listing getListingOrThrow(@NonNull String listingId) {
        Optional<Listing> maybeListing = listingRepository.findById(listingId);
        if (maybeListing.isEmpty()) {
            throw new ResourceNotFoundException("Listing with given listingId does not exists!");
        }
        return maybeListing.get();
    }

}
