package com.dianagrigore.rem.service;

import com.dianagrigore.rem.model.Listing;
import com.dianagrigore.rem.model.Offer;
import com.dianagrigore.rem.model.Review;
import com.dianagrigore.rem.model.User;
import com.dianagrigore.rem.model.enums.UserType;
import com.dianagrigore.rem.repository.AgentListingRepository;
import com.dianagrigore.rem.repository.ListingRepository;
import com.dianagrigore.rem.repository.OfferRepository;
import com.dianagrigore.rem.repository.ReviewRepository;
import com.dianagrigore.rem.repository.UserRepository;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class DataGenerationService {

    private final static List<String> neighbourhood = List.of("Andrei Mureșanu", "Bulgaria", "Buna ziua", "Centru", "Europa", "Manastur", "Zorilor", "Iris", "Marasti",
            "Gheorgheni");
    private final static Random random = new Random();

    private final DataSource dataSource;

    private JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final OfferRepository offerRepository;
    private final AgentListingRepository agentListingRepository;
    private final ListingRepository listingRepository;
    private final Faker faker = new Faker();

    public DataGenerationService(UserRepository userRepository, ReviewRepository reviewRepository, OfferRepository offerRepository, AgentListingRepository agentListingRepository
            , ListingRepository listingRepository, DataSource dataSource) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.offerRepository = offerRepository;
        this.agentListingRepository = agentListingRepository;
        this.listingRepository = listingRepository;
        this.dataSource = dataSource;
    }

    @PostConstruct
    private void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void cleanup() {
        reviewRepository.deleteAllInBatch();
        offerRepository.deleteAllInBatch();
        agentListingRepository.deleteAllInBatch();
        listingRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    public void batch() {
        cleanup();
        generate(0);
    }

    public void millions() {
        for (int i = 1; i < 1000; i++) {
            generate((i - 1) * 1000);
            log.info("{}.{}%", i / 100, i % 100);
        }
    }

    @Transactional
    public void generate(int base) {
        int count = 1000;
        int phoneBase = 700000000;
        String sql = "INSERT INTO app_user (user_id, name, email, password, phone_number, type) VALUES (?, ?, ?, ?, ?, ?)";
        List<Object[]> users = new ArrayList<>();
        User user = new User();
        for (int i = 0; i < count; i++) {
            user.setName(faker.name().fullName());
            user.setEmail(faker.name().username() + (base + i) + "@gmail.com");
            user.setPassword("nJ8IjYf4+Iz4SFkkYqaRhw==");
            user.setPhoneNumber("0" + (phoneBase + base + i));
            user.setType(UserType.DIRECTOR);
            users.add(new Object[]{UUID.randomUUID().toString(), user.getName(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.getType().toString()});
        }
        jdbcTemplate.batchUpdate(sql, users);

        sql = "INSERT INTO listing (listing_id, name, address, rooms, description, size, neighbourhood, suggested_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        List<Object[]> listings = new ArrayList<>();
        Listing listing = new Listing();
        for (int i = 0; i < count; i++) {
            listing.setSuggestedPrice(faker.number().numberBetween(10000, 200000));
            listing.setRooms(faker.number().numberBetween(1, 5));
            listing.setDescription(faker.backToTheFuture().quote());
            listing.setName(faker.funnyName().name());
            listing.setSize(faker.number().numberBetween(20, 120));
            listing.setNeighbourhood(neighbourhood.get(faker.number().numberBetween(0, 9)));
            listing.setAddress(faker.address().fullAddress());
            listings.add(new Object[]{UUID.randomUUID().toString(), listing.getName(), listing.getAddress(), listing.getRooms(), listing.getDescription(), listing.getSize(),
                    listing.getNeighbourhood(), listing.getSuggestedPrice()});
        }
        jdbcTemplate.batchUpdate(sql, listings);

        List<Object[]> agentListings = new ArrayList<>();
        sql = "INSERT INTO agent_listing (listing_id, user_id) VALUES (?, ?)";

        for (int i = 0; i < count; i++) {
            Set<Integer> numbers = new HashSet<>();
            while (numbers.size() < 10) {
                int number = random.nextInt(1000);
                numbers.add(number);
            }
            List<Integer> randomUsers = new ArrayList<>(numbers);
            for (int j = 0; j < 10; j++) {
                agentListings.add(new Object[]{listings.get(i)[0], users.get(randomUsers.get(j))[0]});

            }
        }
        jdbcTemplate.batchUpdate(sql, agentListings);

        sql = "INSERT INTO offer (offer_id, price, comment, timestamp, listing_id, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        List<Object[]> offers = new ArrayList<>();
        Offer offer = new Offer();
        for (int i = 0; i < count; i++) {
            int listing_index = faker.number().numberBetween(0, 999);
            int agent = faker.number().numberBetween(0, 999);
            offer.setComment(faker.chuckNorris().fact());
            offer.setPrice(faker.number().numberBetween(1000, 300000));
            offers.add(new Object[]{UUID.randomUUID().toString(), offer.getPrice(), offer.getComment(), offer.getTimestamp(), listings.get(listing_index)[0], users.get(agent)[0]});
        }
        jdbcTemplate.batchUpdate(sql, offers);

        sql = "INSERT INTO review (review_id, stars, review, timestamp, user_id) VALUES (?, ?, ?, ?, ?)";

        List<Object[]> reviews = new ArrayList<>();
        Review review = new Review();
        for (int i = 0; i < count; i++) {
            int agent = faker.number().numberBetween(0, 999);
            review.setReview(faker.elderScrolls().quote());
            review.setStars(faker.number().numberBetween(1, 5));
            reviews.add(new Object[]{UUID.randomUUID().toString(), review.getStars(), review.getReview(), review.getTimestamp(), users.get(agent)[0]});
        }
        jdbcTemplate.batchUpdate(sql, reviews);
    }

}
