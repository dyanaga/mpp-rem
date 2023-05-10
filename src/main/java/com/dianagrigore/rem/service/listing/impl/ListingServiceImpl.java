package com.dianagrigore.rem.service.listing.impl;

import static java.util.Objects.nonNull;

import com.dianagrigore.rem.dto.ListingDto;
import com.dianagrigore.rem.dto.pages.ListingPage;
import com.dianagrigore.rem.exception.BaseException;
import com.dianagrigore.rem.exception.ResourceNotFoundException;
import com.dianagrigore.rem.model.AgentListing;
import com.dianagrigore.rem.model.Listing;
import com.dianagrigore.rem.model.User;
import com.dianagrigore.rem.repository.AgentListingRepository;
import com.dianagrigore.rem.repository.ListingRepository;
import com.dianagrigore.rem.repository.OfferRepository;
import com.dianagrigore.rem.repository.UserRepository;
import com.dianagrigore.rem.security.SecurityService;
import com.dianagrigore.rem.service.listing.ListingService;
import com.dianagrigore.rem.utils.expand.ExpandableFields;
import com.dianagrigore.rem.web.converter.BasicMapper;
import com.dianagrigore.rem.web.filter.FieldFilter;
import com.dianagrigore.rem.web.filter.FilterOperation;
import com.dianagrigore.rem.web.filter.FilterUtils;
import com.dianagrigore.rem.web.filter.specifications.IntegerSpecifications;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ListingServiceImpl implements ListingService {
    private static final Logger logger = LoggerFactory.getLogger(ListingServiceImpl.class);

    private static final List<String> FIND_ALLOWED_STRING_FILTERS = List.of("name", "address", "description", "neighbourhood","listingId");
    private static final List<String> FIND_ALLOWED_INT_FILTERS = List.of("rooms");
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final AgentListingRepository agentListingRepository;
    private final OfferRepository offerRepository;
    private final BasicMapper<Listing, ListingDto> basicMapper;
    private final SecurityService securityService;

    public ListingServiceImpl(ListingRepository listingRepository, UserRepository userRepository, AgentListingRepository agentListingRepository, OfferRepository offerRepository,
            BasicMapper<Listing, ListingDto> basicMapper, SecurityService securityService) {
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
        this.agentListingRepository = agentListingRepository;
        this.offerRepository = offerRepository;
        this.basicMapper = basicMapper;
        this.securityService = securityService;
    }

    @Override
    public ListingDto createListing(@Valid ListingDto listingToCreate) {
        logger.debug("Started to create listing.");
        Listing listing = basicMapper.convertTarget(listingToCreate);
        listing.setUsers(null);
        Listing savedListing = listingRepository.save(listing);
        String agentId = securityService.getUserId();
        User user = getUserOrThrow(agentId);
        logger.debug("Listing with id {} successfully created.", savedListing.getListingId());

        saveAgentListing(listing, user);

        return basicMapper.convertSource(savedListing);
    }

    @Override
    public ListingPage findListings(@Nullable String filter, @Nullable Integer page, @Nullable Integer pageSize, @Nullable String sort, @Nullable String expand) {
        logger.debug("Find listings called with parameters: filter=[{}], page=[{}], pageSize=[{}], sort=[{}], expand=[{}]", filter, page, pageSize, sort, expand);
        if (nonNull(sort) && sort.contains("price")) {
            sort = sort.replace("price", "suggestedPrice");
        }
        Pageable pageable = PageUtils.getPageable(page, pageSize, sort, "+name");
        Map<String, FieldFilter> fieldFiltersMap = FilterUtils.getFieldFiltersMap(filter);
        if (fieldFiltersMap.containsKey("agent")) {
            FieldFilter agent = fieldFiltersMap.get("agent");
            fieldFiltersMap.remove("agent");
            String listings = agentListingRepository.findAllByUserId(agent.getValue()).stream().map(AgentListing::getListingId).collect(Collectors.joining(","));
            fieldFiltersMap.put("listingId", new FieldFilter().setValue(listings).setFilterOperation(FilterOperation.IN).setFieldName("listingId"));
        }

        List<Specification<Listing>> specifications = new ArrayList<>();
        StringSpecifications.appendSpecifications(specifications, fieldFiltersMap, FIND_ALLOWED_STRING_FILTERS);
        IntegerSpecifications.appendSpecifications(specifications, fieldFiltersMap, FIND_ALLOWED_INT_FILTERS);

        Specification<Listing> specification = specifications.stream().reduce(Specification::and).orElse(null);

        Page<Listing> resultPage = listingRepository.findAll(specification, pageable);
        logger.debug("Page of size=[{}] successfully got from the repository.", resultPage.getNumberOfElements());
        return new ListingPage(resultPage, basicMapper.convertSource(resultPage.getContent(), expand));
    }

    @Override
    public ListingDto getListing(@NonNull String listingId) {
        logger.debug("Get listing by id=[{}] requested.", listingId);
        Listing listing = getListingOrThrow(listingId);
        logger.debug("Listing with id=[{}] successfully found.", listingId);
        return basicMapper.convertSource(listing, ExpandableFields.USERS.getStringValue());
    }

    @Override
    public ListingDto updateListing(@NonNull String listingId, @Valid ListingDto listingWithUpdates) {
        logger.debug("Update listing by id=[{}] requested with body=[{}]", listingId, listingWithUpdates);
        Listing listing = getListingOrThrow(listingId);

        listing.setName(listingWithUpdates.getName());
        listing.setAddress(listingWithUpdates.getAddress());
        listing.setDescription(listingWithUpdates.getDescription());
        listing.setNeighbourhood(listingWithUpdates.getNeighbourhood());
        listing.setRooms(listingWithUpdates.getRooms());
        listing.setSize(listingWithUpdates.getSize());
        listing.setSuggestedPrice(listingWithUpdates.getSuggestedPrice());

        Listing save = listingRepository.save(listing);

        return basicMapper.convertSource(save);
    }

    @Override
    public ListingDto deleteListing(@NonNull String listingId) {
        logger.debug("Delete listing by id=[{}] requested.", listingId);
        Listing listing = getListingOrThrow(listingId);

        agentListingRepository.deleteByListingId(listingId);
        offerRepository.deleteByListingId(listingId);
        listingRepository.delete(listing);

        logger.debug("Successfully deleted listing id=[{}].", listingId);
        return basicMapper.convertSource(listing);
    }

    @Override
    public ListingDto enrollListing(String listingId) {
        String agentId = securityService.getUserId();
        return enrollUser(listingId, agentId);
    }

    @Override
    public ListingDto enrollListingSpecific(String listingId, String userId) {
        return enrollUser(listingId, userId);
    }

    @Override
    public ListingDto quitListing(String listingId) {
        String agentId = securityService.getUserId();
        return quit(listingId, agentId);
    }

    @Override
    public ListingDto quitListingSpecific(String listingId, String userId) {
        return quit(listingId, userId);
    }

    private ListingDto enrollUser(String listingId, String agentId) {
        Listing listing = getListingOrThrow(listingId);
        User user = getUserOrThrow(agentId);

        List<AgentListing> users = listing.getUsers();
        boolean userEnrolled = users.stream().anyMatch(agentListing -> agentId.equals(agentListing.getUser().getUserId()));
        if (userEnrolled) {
            throw new BaseException("User already enrolled.");
        }
        AgentListing save = saveAgentListing(listing, user);

        List<AgentListing> agentListings = new ArrayList<>(users);
        agentListings.add(save);
        listing.setUsers(agentListings);

        return basicMapper.convertSource(listing, ExpandableFields.USERS.getStringValue());
    }

    private ListingDto quit(String listingId, String agentId) {
        Listing listing = getListingOrThrow(listingId);
        User user = getUserOrThrow(agentId);

        List<AgentListing> users = listing.getUsers();
        Optional<AgentListing> maybeAgentListing = users.stream().filter(agentListing -> agentId.equals(agentListing.getUser().getUserId())).findFirst();
        if (maybeAgentListing.isEmpty()) {
            throw new BaseException("User not enrolled.");
        }

        agentListingRepository.delete(maybeAgentListing.get());
        logger.debug("User with id {} successfully removed to listing {}.", user.getUserId(), listing.getListingId());
        List<AgentListing> agentListings = users.stream().filter(agentListing -> !agentId.equals(agentListing.getUser().getUserId())).toList();
        listing.setUsers(agentListings);
        return basicMapper.convertSource(listing, ExpandableFields.USERS.getStringValue());
    }

    private AgentListing saveAgentListing(Listing listing, User user) {
        AgentListing agentListing = new AgentListing();
        agentListing.setUser(user);
        agentListing.setUserId(user.getUserId());
        agentListing.setListing(listing);
        agentListing.setListingId(listing.getListingId());
        AgentListing save = agentListingRepository.save(agentListing);
        logger.debug("User with id {} successfully added to listing {}.", user.getUserId(), listing.getListingId());
        return save;
    }

    private Listing getListingOrThrow(@NonNull String listingId) {
        Optional<Listing> maybeListing = listingRepository.findById(listingId);
        if (maybeListing.isEmpty()) {
            throw new ResourceNotFoundException("Listing with this listingId does not exists!");
        }
        return maybeListing.get();
    }

    private User getUserOrThrow(@NonNull String userId) {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new ResourceNotFoundException("User with given userId does not exists!");
        }
        return maybeUser.get();
    }

}
