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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

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
    private final ApplicationContext applicationContext;


    public DataGenerationService(UserRepository userRepository, ReviewRepository reviewRepository, OfferRepository offerRepository, AgentListingRepository agentListingRepository
            , ListingRepository listingRepository, DataSource dataSource, ApplicationContext applicationContext) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.offerRepository = offerRepository;
        this.agentListingRepository = agentListingRepository;
        this.listingRepository = listingRepository;
        this.dataSource = dataSource;
        this.applicationContext = applicationContext;
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
        disableAll();
        try {
            ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) applicationContext.getBean("taskExecutor");
            for (int i = 0; i < 100; i++) {
                CountDownLatch latch = new CountDownLatch(10);
                for (int j = 0; j < 10; j++) {
                    int index = i * 10 + j;
                    executor.execute(() -> {
                        try {
                            generate(index * 1000);
                        } finally {
                            latch.countDown();
                        }
                    });
                }
                latch.await();
                log.info("{}.{}%", (i + 1), (i + 1) % 10 * 10);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            enableAll();
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
            offer.setTimestamp(new Date());
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
            review.setTimestamp(new Date());
            reviews.add(new Object[]{UUID.randomUUID().toString(), review.getStars(), review.getReview(), review.getTimestamp(), users.get(agent)[0]});
        }
        jdbcTemplate.batchUpdate(sql, reviews);
    }

    private void disableAll() {
        jdbcTemplate.execute("ALTER TABLE app_user DISABLE TRIGGER ALL");
        jdbcTemplate.execute("ALTER TABLE listing DISABLE TRIGGER ALL");
        jdbcTemplate.execute("ALTER TABLE agent_listing DISABLE TRIGGER ALL");
        jdbcTemplate.execute("ALTER TABLE offer DISABLE TRIGGER ALL");
        jdbcTemplate.execute("ALTER TABLE review DISABLE TRIGGER ALL");
    }

    private void enableAll() {
        jdbcTemplate.execute("ALTER TABLE app_user ENABLE TRIGGER ALL");
        jdbcTemplate.execute("ALTER TABLE listing ENABLE TRIGGER ALL");
        jdbcTemplate.execute("ALTER TABLE agent_listing ENABLE TRIGGER ALL");
        jdbcTemplate.execute("ALTER TABLE offer ENABLE TRIGGER ALL");
        jdbcTemplate.execute("ALTER TABLE review ENABLE TRIGGER ALL");
    }

}
