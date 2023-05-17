package com.dianagrigore.rem.service.user.impl;

import static java.util.Objects.isNull;

import com.dianagrigore.rem.dto.RegistrationDto;
import com.dianagrigore.rem.dto.UserDto;
import com.dianagrigore.rem.dto.pages.UserPage;
import com.dianagrigore.rem.dto.token.AuthenticationType;
import com.dianagrigore.rem.exception.BaseException;
import com.dianagrigore.rem.exception.ResourceNotFoundException;
import com.dianagrigore.rem.model.UserLogin;
import com.dianagrigore.rem.model.User;
import com.dianagrigore.rem.model.enums.UserType;
import com.dianagrigore.rem.repository.AgentListingRepository;
import com.dianagrigore.rem.repository.ListingRepository;
import com.dianagrigore.rem.repository.OfferRepository;
import com.dianagrigore.rem.repository.UserLoginRepository;
import com.dianagrigore.rem.repository.ReviewRepository;
import com.dianagrigore.rem.repository.UserRepository;
import com.dianagrigore.rem.security.SecurityService;
import com.dianagrigore.rem.service.user.UserService;
import com.dianagrigore.rem.utils.expand.ExpandBuilder;
import com.dianagrigore.rem.utils.expand.ExpandableFields;
import com.dianagrigore.rem.web.converter.BasicMapper;
import com.dianagrigore.rem.web.filter.FieldFilter;
import com.dianagrigore.rem.web.filter.FilterUtils;
import com.dianagrigore.rem.web.filter.specifications.StringSpecifications;
import com.dianagrigore.rem.web.paging.PageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final List<String> FIND_ALLOWED_STRING_FILTERS = List.of("name", "phoneNumber", "email");
    private final UserRepository userRepository;
    private final UserLoginRepository userLoginRepository;
    private final ReviewRepository reviewRepository;
    private final OfferRepository offerRepository;
    private final AgentListingRepository agentListingRepository;
    private final ListingRepository listingRepository;
    private final BasicMapper<User, UserDto> basicMapper;
    private final BasicMapper<UserLogin, RegistrationDto> registrationMapper;
    private final SecurityService securityService;

    public UserServiceImpl(UserRepository userRepository, UserLoginRepository userLoginRepository, ReviewRepository reviewRepository, OfferRepository offerRepository,
            AgentListingRepository agentListingRepository, ListingRepository listingRepository, BasicMapper<User, UserDto> basicMapper, BasicMapper<UserLogin, RegistrationDto> registrationMapper,
            SecurityService securityService) {
        this.userRepository = userRepository;
        this.userLoginRepository = userLoginRepository;
        this.reviewRepository = reviewRepository;
        this.offerRepository = offerRepository;
        this.agentListingRepository = agentListingRepository;
        this.listingRepository = listingRepository;
        this.basicMapper = basicMapper;
        this.registrationMapper = registrationMapper;
        this.securityService = securityService;
    }

    @Override
    public UserDto createUser(@Valid UserDto userToCreate) {
        logger.debug("Started to create user.");
        if (UserType.GUEST.equals(userToCreate.getType())) {
            throw new BaseException("Unable to create guest account.");
        }
        if (UserType.ADMIN.equals(userToCreate.getType()) && !securityService.getAuthenticationType().equals(AuthenticationType.ADMIN)) {
            throw new BaseException("Unable to create admin account.");
        }
        validateUserCreate(userToCreate);

        User user = basicMapper.convertTarget(userToCreate);
        User savedUser = userRepository.save(user);

        UserLogin userLogin = new UserLogin();
        userLogin.setUserId(savedUser.getUserId());
        userLogin.setUser(savedUser);
        userLogin.setUsername(userToCreate.getUsername());
        userLogin.setPassword(userToCreate.getPassword());
        userLogin.setActive(true);
        userLogin.setTimestamp(new Date());

        UserLogin savedLogin = userLoginRepository.save(userLogin);
        savedUser.setLogin(savedLogin);
        logger.debug("User with id {} successfully created.", savedUser.getUserId());

        return basicMapper.convertSource(savedUser);
    }

    @Override
    public RegistrationDto registerUser(UserDto userToRegister) {
        logger.debug("Started to register user.");
        validateUserCreate(userToRegister);
        userToRegister.setType(UserType.CLIENT);
        User user = basicMapper.convertTarget(userToRegister);
        User savedUser = userRepository.save(user);

        UserLogin userLogin = new UserLogin();
        userLogin.setUserId(savedUser.getUserId());
        userLogin.setUsername(userToRegister.getUsername());
        userLogin.setPassword(userToRegister.getPassword());
        userLogin.setActive(false);
        userLogin.setTimestamp(new Date());

        UserLogin savedUserLogin = userLoginRepository.save(userLogin);
        logger.debug("User with id {} successfully registered.", savedUser.getUserId());

        return registrationMapper.convertSource(savedUserLogin);
    }

    private void validateUserCreate(UserDto userToRegister) {
        if (isNull(userToRegister.getPassword())) {
            throw new BaseException("No password given.");
        }
        if(userLoginRepository.findUserLoginByUsername(userToRegister.getUsername()).isPresent()) {
            throw new BaseException("Username already used");
        }
        if(userRepository.findFirstByEmail(userToRegister.getEmail()).isPresent()) {
            throw new BaseException("Email already used");
        }
    }

    @Override
    public UserDto activateUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BaseException("User confirmation code invalid or invalid id."));
        UserLogin userLogin = userLoginRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Registration not found."));
        if (Duration.between(userLogin.getTimestamp().toInstant(), new Date().toInstant()).toMinutes() > 10) {
            throw new BaseException("Registration timed out");
        }

        userLogin.setActive(true);
        UserLogin save = userLoginRepository.save(userLogin);
        user.setLogin(save);
        logger.debug("User with id {} successfully activated.", user.getUserId());

        return basicMapper.convertSource(user);
    }

    @Override
    @Scheduled(fixedRate = 1200000, initialDelay = 15000) // run every 10 minutes (in milliseconds)
    @Transactional
    public void removeOldRegistrations() {
        Date now = new Date();
        Date tenMinutesAgo = new Date(now.getTime() - 10 * 60 * 1000);
        List<UserLogin> allByTimestampBefore = userLoginRepository.findAllByTimestampBeforeAndIsActiveIsFalse(tenMinutesAgo);
        for (UserLogin userLogin : allByTimestampBefore) {
            try {
                userLoginRepository.deleteById(userLogin.getUserId());
                userRepository.deleteById(userLogin.getUserId());
                logger.debug("User with id {} deleted before registration.", userLogin.getUserId());
            } catch (Exception e) {
                logger.error("User with id {} was not deleted before registration, will try again in 20 minutes.", userLogin.getUserId(), e);
            }
        }
    }

    @Override
    public UserPage findUsers(@Nullable String filter, @Nullable Integer page, @Nullable Integer pageSize, @Nullable String sort, @Nullable String expand) {
        logger.debug("Find users called with parameters: filter=[{}], page=[{}], pageSize=[{}], sort=[{}], expand=[{}]", filter, page, pageSize, sort, expand);
        Pageable pageable = PageUtils.getPageable(page, pageSize, sort, "+name");
        Map<String, FieldFilter> fieldFiltersMap = FilterUtils.getFieldFiltersMap(filter);

        List<Specification<User>> specifications = new ArrayList<>();
        StringSpecifications.appendSpecifications(specifications, fieldFiltersMap, FIND_ALLOWED_STRING_FILTERS);

        Specification<User> specification = specifications.stream().reduce(Specification::and).orElse(null);

        Page<User> resultPage = userRepository.findAll(specification, pageable);
        logger.debug("Page of size=[{}] successfully got from the repository.", resultPage.getNumberOfElements());
        return new UserPage(resultPage, basicMapper.convertSource(resultPage.getContent(), expand));
    }

    @Override
    public UserDto getUser(@NonNull String userId) {
        logger.debug("Get user by id=[{}] requested.", userId);
        User user = getUserOrThrow(userId);
        logger.debug("User with id=[{}] successfully found.", userId);
        String valueOf = ExpandBuilder.of(ExpandableFields.OFFERS)
                .and(ExpandableFields.REVIEWS)
                .and(ExpandableFields.WRITTEN_REVIEWS)
                .and(ExpandableFields.LISTINGS_CREATED)
                .and(ExpandableFields.LISTINGS).toString();
        return basicMapper.convertSource(user, valueOf);
    }

    @Override
    public UserDto getUserProfile() {
        String userId = securityService.getUserId();
        return getUser(userId);
    }

    @Override
    public UserDto updateUser(@NonNull String userId, @Valid UserDto userWithUpdates) {
        logger.debug("Update user by id=[{}] requested with body=[{}]", userId, userWithUpdates);
        User user = getUserOrThrow(userId);

        if (userId.equals(securityService.getUserId()) && user.getType() != userWithUpdates.getType()) {
            throw new BaseException("You are not allowed to modify your type");
        }
        if(!user.getEmail().equals(userWithUpdates.getEmail()) && userRepository.findFirstByEmail(userWithUpdates.getEmail()).isPresent()) {
            throw new BaseException("Email already used");
        }
        if(!user.getLogin().getUsername().equals(userWithUpdates.getUsername()) && userLoginRepository.findUserLoginByUsername(userWithUpdates.getUsername()).isPresent()) {
            throw new BaseException("Username already used");
        }

        user.setName(userWithUpdates.getName());
        user.setEmail(userWithUpdates.getEmail());
        user.setPhoneNumber(userWithUpdates.getPhoneNumber());
        user.setBio(userWithUpdates.getBio());
        user.setLocation(userWithUpdates.getLocation());
        user.setGender(userWithUpdates.getGender());
        user.setBirthday(userWithUpdates.getBirthday());
        user.setUrl(userWithUpdates.getUrl());
        user.setPagePreference(userWithUpdates.getPagePreference());
        user.getLogin().setUsername(userWithUpdates.getUsername());
        user.setType(userWithUpdates.getType());

        if (Objects.nonNull(userWithUpdates.getPassword())) {
            user.getLogin().setPassword(userWithUpdates.getPassword());
        }
        User save = userRepository.save(user);
        userLoginRepository.save(user.getLogin());
        return basicMapper.convertSource(save);
    }

    @Override
    public UserDto deleteUser(@NonNull String userId) {
        logger.debug("Delete user by id=[{}] requested.", userId);
        User user = getUserOrThrow(userId);
        agentListingRepository.deleteByAgentId(userId);
        listingRepository.deleteByCreator(userId);
        reviewRepository.deleteByUserId(userId);
        reviewRepository.deleteByCreator(userId);
        offerRepository.deleteByUserId(userId);
        userLoginRepository.deleteById(userId);
        userRepository.deleteById(userId);

        logger.debug("Successfully deleted user id=[{}].", userId);
        return basicMapper.convertSource(user);
    }

    @Override
    public void setPagePreferenceForAllUsers(int pagePreference) {
        userRepository.setPagePreferenceForAllUsers(pagePreference);
    }

    private User getUserOrThrow(@NonNull String userId) {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            throw new ResourceNotFoundException("User with this userId does not exists!");
        }
        return maybeUser.get();
    }

}
